package com.inari.firefly.system.component

import com.inari.firefly.*
import com.inari.firefly.component.*
import com.inari.firefly.system.FFSystem
import com.inari.util.aspect.Aspects
import com.inari.util.collection.DynArray
import com.inari.util.collection.DynArrayRO
import com.inari.util.collection.DynIntArrayRO
import java.util.*

interface ComponentSystem : FFSystem {

    val supportedComponents: Aspects

    companion object {
        fun <C : NamedComponent> createComponentMapping(
            type: ComponentType<C>,
            size: Int = 20,
            exp: Int = 10,
            activationMapping: Boolean = false,
            nameMapping: Boolean = false,
            listener:(C, ComponentMap.MapAction) -> Unit = { _, _ -> },
            mapToContext: Boolean = true
        ): ComponentMap<C> {
            val mapper: ComponentMapImpl<C> = ComponentMapImpl(
                type, size, exp, activationMapping, nameMapping, listener
            )
            if (mapToContext)
                FFContext.componentMaps[mapper.typeIndex] = mapper
            return mapper
        }
    }

    private class ComponentMapImpl<C : NamedComponent>(
        override val componentType: ComponentType<C>,
        size: Int,
        exp: Int,
        override val activationMapping: Boolean,
        override  val nameMapping: Boolean,
        private val listener: (C, ComponentMap.MapAction) -> Unit
    ) : ComponentMap<C> {

        override val typeIndex: Int = componentType.aspectIndex
        @Suppress("UNCHECKED_CAST")
        private val _map: DynArray<C> = DynArray.of(componentType.typeClass as Class<C>, size, exp)
        override val map: DynArrayRO<C> = _map
        private val active: BitSet = BitSet()
        private val nameMap: MutableMap<String, C>? = if (nameMapping) HashMap() else null

        override operator fun contains(index: Int): Boolean = map.contains(index)
        override operator fun contains(name: String): Boolean = internalIndexForName(name) >= 0

        override fun activate(index: Int) {
            if (activationMapping && !isActive(index)) {
                active.set(index)
                listener(_map[index]!!, ComponentMap.MapAction.ACTIVATED)
            }
        }

        override fun deactivate(index: Int) {
            if (isActive(index)) {
                active.set(index, false)
                listener(_map[index]!!,  ComponentMap.MapAction.DEACTIVATED)
            }
        }

        override fun isActive(index: Int): Boolean =
            _map.contains(index) && active.get(index)

        override fun activate(name: String) = activate(indexForName(name))
        override fun deactivate(name: String) = deactivate(indexForName(name))
        override fun isActive(name: String): Boolean = isActive(indexForName(name))
        override operator fun get(index: Int): C = _map[index]!!
        override operator fun get(name: String): C = this[indexForName(name)]
        @Suppress("UNCHECKED_CAST")
        override fun <CC : C> getAs(index: Int): CC = this[index] as CC
        @Suppress("UNCHECKED_CAST")
        override fun <CC : C> getAs(name: String): CC = this[name] as CC
        override fun contains(id: CompId): Boolean = contains(id.checkType(componentType).instanceId)
        override fun activate(id: CompId) = activate(id.checkType(componentType).instanceId)
        override fun deactivate(id: CompId) = deactivate(id.checkType(componentType).instanceId)
        override fun isActive(id: CompId): Boolean = isActive(id.checkType(componentType).instanceId)
        override fun get(id: CompId): C = get(id.checkType(componentType).instanceId)
        override fun <CC : C> getAs(id: CompId): CC = getAs(id.checkType(componentType).instanceId)
        override fun delete(id: CompId) = delete(id.checkType(componentType).instanceId)
        override fun delete(name: String) = delete(indexForName(name))

        override fun delete(index: Int) {
            if (_map.contains(index)) {

                // first remove from map
                val c: C = _map.remove(index)!!
                if (nameMapping)
                    nameMap?.remove(c.name)

                // if still active, deactivate
                if (activationMapping) {
                    active.set(index, false)
                    listener(c, ComponentMap.MapAction.DEACTIVATED )
                }

                // notify and dispose
                listener(c, ComponentMap.MapAction.DELETED )
                c.dispose()
            }
        }

        override fun deleteAll(predicate: Predicate<C>) =
            _map.filter(predicate)
                .forEach { comp -> delete(comp.index) }

        override fun idForName(name: String): CompId {
            if (nameMapping) {
                return nameMap?.get(name)?.componentId ?:
                    throw RuntimeException("Component: $componentType for name: $name not found")
            }

            val id: Int = (0 until _map.capacity).firstOrNull {
                it in _map && _map[it]?.name == name
            } ?: -1

            return when(id) {
                -1 -> throw RuntimeException("Component: $componentType for name: $name not found")
                else -> map[id]?.componentId ?: NO_COMP_ID
            }
        }

        override fun indexForName(name: String): Int {
            val result = internalIndexForName(name)
            if (result < 0)
                throw RuntimeException("Component: $componentType for name: $name not found")

            return result
        }

        private fun internalIndexForName(name: String): Int {
            if (nameMapping)
                return nameMap?.get(name)?.index ?: -1

            return (0 until _map.capacity).firstOrNull {
                it in _map && _map[it]?.name == name
            } ?: -1
        }

        override fun nextActive(from: Int): Int =
            active.nextSetBit(from)

        fun nextIndex(predicate: Predicate<C>, currentIndex: Int): Int =
            if (currentIndex >= map.capacity)
                -1
            else if (map.contains(currentIndex) && predicate(map[currentIndex]!!))
                currentIndex
            else
                nextIndex(predicate, currentIndex + 1)

        override fun indexIterator(predicate: Predicate<C>): IntFunction = object : IntFunction {
            override fun invoke(i: Int): Int {
                return nextIndex(predicate, i)
            }
        }

        override fun receiver(): Receiver<C> = { c -> add(c) }

        override fun forEach(expr: Consumer<C>) =
            map.forEach{ c -> expr(c) }

        override fun forEachActive(expr: (C) -> Unit) {
            var i = active.nextSetBit(0)
            while (i >= 0) {
                expr(map[i]!!)
                i = active.nextSetBit(i+1)
            }
        }

        override fun forEachIn(bag: DynIntArrayRO, expr: Consumer<C>) {
            val i = bag.iterator()
            while (i.hasNext())
                expr(map[i.next()]!!)
        }

        override fun <CC : C> forEachSubtypeIn(bag: DynIntArrayRO, expr: Consumer<CC>) {
            val i = bag.iterator()
            while (i.hasNext()) {
                @Suppress("UNCHECKED_CAST")
                expr(map[i.next()] as CC)
            }
        }

        override fun clear() {
            for (c in _map)
                delete(c.index)

            _map.clear()
            if (nameMapping)
                nameMap?.clear()
            if (activationMapping)
                active.clear()
        }


        private fun <CC : C> add(c: CC, alsoActivate: Boolean = false): CC {
            _map[c.index] = c
            listener(c, ComponentMap.MapAction.CREATED)
            if (nameMapping && c.name !== NO_NAME)
                nameMap?.put(c.name, c)
            if (alsoActivate)
                activate(c.index)
            return c
        }
    }
}