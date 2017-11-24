package com.inari.firefly.component.mapping

import com.inari.commons.lang.list.DynArray
import com.inari.commons.lang.list.DynArrayRO
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentType
import com.inari.firefly.component.NamedComponent
import com.inari.firefly.system.Constants.NO_NAME
import java.util.BitSet
import kotlin.collections.HashMap


class ComponentMap<C : NamedComponent>(
        private val type: ComponentType<C>,
        size: Int = 20,
        exp: Int = 10,
        val activationMapping: Boolean = false,
        val nameMapping: Boolean = false,
        private val listener: (CompId, IComponentMap.MapAction) -> Unit = { id, _ -> id }
) : IComponentMap<C> {

    override fun type(): ComponentType<C> = type
    private val _map: DynArray<C> = DynArray.create(type.typeKey.type(), size, exp)
    val map: DynArrayRO<C> = _map
    private val active: BitSet? = if (activationMapping) BitSet() else null
    private val nameMap: MutableMap<String, C>? = if (nameMapping) HashMap() else null

    override fun activate(id: Int) {
        if (activationMapping && !_isActive(id)) {
            active?.set(id)
            listener(
                _map.get(id).componentId,
                IComponentMap.MapAction.ACTIVATED
            )
        }
    }

    override fun deactivate(id: Int) {
        if (isActive(id)) {
            active?.set(id, false)
            listener(
                _map.get(id).componentId,
                IComponentMap.MapAction.DEACTIVATED
            )
        }
    }

    override fun isActive(id: Int): Boolean =
            activationMapping && _isActive(id)
    private fun _isActive(id: Int): Boolean =
            _map.contains(id) && active?.get(id) ?: false

    override fun activate(name: String) = activate(getId(name))
    override fun deactivate(name: String) = deactivate(getId(name))
    override fun isActive(name: String): Boolean = isActive(getId(name))
    override fun get(id: Int): C = _map.get(id)
    override fun get(name: String): C = get(getId(name))
    override fun delete(name: String) = delete(getId(name))

    override fun delete(id: Int) {
        if (_map.contains(id)) {
            val c: C = _map.remove(id)
            if (nameMapping) {
                nameMap?.remove(c.name())
            }
            if (activationMapping) {
                active?.set(id, false)
            }
            listener(
                c.componentId,
                IComponentMap.MapAction.DELETED
            )
            c.dispose()
        }
    }

    override fun getId(name: String): Int {
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

    override fun receiver(): (C) -> C = { c -> add(c) }

    fun clear() {
        for (c in _map) {
            delete(c.index())
        }
        _map.clear()
        if (nameMapping) {
            nameMap?.clear()
        }
        if (activationMapping) {
            active?.clear()
        }
    }

    private fun <CC : C> add(c: CC, alsoActivate: Boolean = false): CC {
        _map.set(c.index(), c)
        listener(
            c.componentId,
            IComponentMap.MapAction.CREATED
        )
        if (nameMapping && c.name() != NO_NAME ) {
            nameMap?.put(c.name(), c)
        }
        if (alsoActivate) {
            activate(c.index())
        }
        return c
    }

}