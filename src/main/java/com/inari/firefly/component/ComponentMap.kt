package com.inari.firefly.component

import com.inari.commons.StringUtils
import com.inari.commons.lang.list.DynArray
import com.inari.commons.lang.list.DynArrayRO
import java.util.BitSet



class ComponentMap<C : NamedComponent>(
        private val type: ComponentType<C>,
        size: Int = 20,
        exp: Int = 10,
        val activationMapping: Boolean = false,
        val nameMapping: Boolean = false
) : IComponentMap<C> {

    override fun type(): ComponentType<C> = type
    private val _map: DynArray<C> = DynArray.create(type.typeKey.type(), size, exp)
    val map: DynArrayRO<C> = _map
    private val active: BitSet? = if (activationMapping) BitSet() else null
    private val nameMap: MutableMap<String, C>? = if (nameMapping) HashMap() else null
    fun clear() = _map.clear()

    override fun activate(id: Int) {
        if (!isActive(id)) {
            active?.set(id)
        }
    }

    override fun deactivate(id: Int) {
        if (isActive(id)) {
            active?.set(id, false)
        }
    }

    override fun isActive(id: Int): Boolean =
            activationMapping && _map.contains(id) && active?.get(id) ?: false

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

        }
    }

    override fun getId(name: String): Int {
        if (nameMapping) {
            return nameMap?.get(name)?.index() ?:
                    throw RuntimeException("Component: ${type.typeKey} for name: $name not found")
        }

        val id = _map.get(name)
        return when(id) {
            -1 -> throw RuntimeException("Component: ${type.typeKey} for name: $name not found")
            else -> id
        }
    }

    override fun receiver(): (C) -> C = { c -> add(c) }

    private fun <CC : C> add(c: CC, alsoActivate: Boolean = false): CC {
        _map.set(c.index(), c)
        if (nameMapping && !StringUtils.isBlank(c.name()) ) {
            nameMap?.put(c.name(), c)
        }
        if (alsoActivate) {
            activate(c.index())
        }
        return c
    }

    private fun <T : NamedComponent> DynArray<T>.get(name: String): Int =
            (0 until this.capacity()).firstOrNull {
                this.array[it] != null && this.array[it].name() == name
            } ?: -1

}