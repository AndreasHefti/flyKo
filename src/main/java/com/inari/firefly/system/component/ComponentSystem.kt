package com.inari.firefly.system.component

import com.inari.commons.lang.aspect.IAspects
import com.inari.commons.lang.list.DynArray
import com.inari.commons.lang.list.DynArrayRO
import com.inari.commons.lang.list.IntBagRO
import com.inari.firefly.*
import com.inari.firefly.component.*
import com.inari.firefly.system.FFSystem
import java.util.*

interface ComponentSystem : FFSystem {

    val supportedComponents: IAspects

    companion object {
        fun <C : NamedComponent> createComponentMapping(
            type: ComponentType<C>,
            size: Int = 20,
            exp: Int = 10,
            activationMapping: Boolean = false,
            nameMapping: Boolean = false,
            listener:(C, IComponentMap.MapAction) -> Unit = { _, _ -> }
        ): IComponentMap<C> {
            val mapper: ComponentMap<C> = ComponentMap(
                type, size, exp, activationMapping, nameMapping, listener
            )
            FFContext.componentMaps.set(mapper.typeIndex, mapper)
            return mapper
        }
    }

    private class ComponentMap<C : NamedComponent>(
        override val type: ComponentType<C>,
        size: Int,
        exp: Int,
        override val activationMapping: Boolean,
        override  val nameMapping: Boolean,
        private val listener: (C, IComponentMap.MapAction) -> Unit
    ) : IComponentMap<C> {

        override val typeIndex: Int = type.typeKey.index()
        private val _map: DynArray<C> = DynArray.create(type.typeKey.type(), size, exp)
        override val map: DynArrayRO<C> = _map
        private val active: BitSet = BitSet()
        private val nameMap: MutableMap<String, C>? = if (nameMapping) HashMap() else null

        override operator fun contains(index: Int): Boolean = map.contains(index)
        override operator fun contains(name: String): Boolean = map.contains(indexForName(name))

        override fun activate(index: Int) {
            if (activationMapping && !isActive(index)) {
                active.set(index)
                listener(_map.get(index), IComponentMap.MapAction.ACTIVATED)
            }
        }

        override fun deactivate(index: Int) {
            if (isActive(index)) {
                active.set(index, false)
                listener(_map.get(index),  IComponentMap.MapAction.DEACTIVATED)
            }
        }

        override fun isActive(index: Int): Boolean =
            _map.contains(index) && active.get(index)

        override fun activate(name: String) = activate(indexForName(name))
        override fun deactivate(name: String) = deactivate(indexForName(name))
        override fun isActive(name: String): Boolean = isActive(indexForName(name))
        override operator fun get(index: Int): C = _map.get(index)
        override operator fun get(name: String): C = get(indexForName(name))
        @Suppress("UNCHECKED_CAST")
        override fun <CC : C> getAs(index: Int): CC = get(index) as CC
        @Suppress("UNCHECKED_CAST")
        override fun <CC : C> getAs(name: String): CC = get(name) as CC
        override fun delete(name: String) = delete(indexForName(name))

        override fun delete(index: Int) {
            if (_map.contains(index)) {

                // first remove from map
                val c: C = _map.remove(index)
                if (nameMapping) {
                    nameMap?.remove(c.name())
                }

                // if still active, deactivate
                if (activationMapping) {
                    active.set(index, false)
                    listener(c, IComponentMap.MapAction.DEACTIVATED )
                }

                // notify and dispose
                listener(c, IComponentMap.MapAction.DELETED )
                c.dispose()
            }
        }

        override fun deleteAll(predicate: Predicate<C>) =
            _map.filter(predicate)
                .forEach { comp -> delete(comp.index()) }

        override fun idForName(name: String): CompId {
            if (nameMapping) {
                return nameMap?.get(name)?.componentId ?:
                    throw RuntimeException("Component: ${type.typeKey} for name: $name not found")
            }

            val id: Int = (0 until _map.capacity()).firstOrNull {
                _map[it] != null && _map[it].name() == name
            } ?: -1

            return when(id) {
                -1 -> throw RuntimeException("Component: ${type.typeKey} for name: $name not found")
                else -> map.get(id).componentId
            }
        }

        override fun indexForName(name: String): Int {
            if (nameMapping) {
                return nameMap?.get(name)?.index() ?:
                    throw RuntimeException("Component: ${type.typeKey} for name: $name not found")
            }

            val id: Int = (0 until _map.capacity()).firstOrNull {
                _map[it] != null && _map[it].name() == name
            } ?: -1

            return when(id) {
                -1 -> throw RuntimeException("Component: ${type.typeKey} for name: $name not found")
                else -> id
            }
        }

        fun nextIndex(predicate: Predicate<C>, currentIndex: Int): Int =
            if (currentIndex >= map.capacity() )
                -1
            else if (map.contains(currentIndex) && predicate(map.get(currentIndex)))
                currentIndex
            else
                nextIndex(predicate, currentIndex + 1)

        override fun indexIterator(predicate: Predicate<C>): IntFunction = object : IntFunction {
            override fun invoke(i: Int): Int {
                return nextIndex(predicate, i)
            }
        }

        override fun receiver(): Receiver<C> = { c -> add(c) }

        override  fun forEach(expr: Expr<C>) =
            map.forEach({ c -> expr(c) })

        override fun forEachActive(expr: (C) -> Unit) {
            var i = active.nextSetBit(0)
            while (i >= 0) {
                expr(map.get(i))
                i = active.nextSetBit(i+1)
            }
        }

        override fun forEachIn(bag: IntBagRO, expr: Expr<C>) {
            val i = bag.iterator()
            while (i.hasNext()) {
                expr(map.get(i.next()))
            }
        }

        override fun <CC : C> forEachSubtypeIn(bag: IntBagRO, expr: Expr<CC>) {
            val i = bag.iterator()
            while (i.hasNext()) {
                @Suppress("UNCHECKED_CAST")
                expr(map.get(i.next()) as CC)
            }
        }

        override fun clear() {
            for (c in _map) {
                delete(c.index())
            }
            _map.clear()
            if (nameMapping) {
                nameMap?.clear()
            }
            if (activationMapping) {
                active.clear()
            }
        }

        private fun <CC : C> add(c: CC, alsoActivate: Boolean = false): CC {
            _map.set(c.index(), c)
            listener(c, IComponentMap.MapAction.CREATED)
            if (nameMapping && c.name() !== NO_NAME) {
                nameMap?.put(c.name(), c)
            }
            if (alsoActivate) {
                activate(c.index())
            }
            return c
        }
    }
}