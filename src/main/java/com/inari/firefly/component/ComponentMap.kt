package com.inari.firefly.component

import com.inari.commons.StringUtils
import com.inari.commons.lang.list.DynArray
import com.inari.commons.lang.list.DynArrayRO
import java.util.BitSet



class ComponentMap<C : NamedComponent>(
        size: Int = 20,
        exp: Int = 10,
        private val cBuilder: ComponentBuilder<C>,
        val activationMapping: Boolean = false,
        val nameMapping: Boolean = false
) : IComponentMap<C> {

    private val _map: DynArray<C> = DynArray.create(cBuilder.typeKey.type(), size, exp)
    val map: DynArrayRO<C> = _map
    private val active: BitSet? = if (activationMapping) BitSet() else null
    private val nameMap: MutableMap<String, C>? = if (nameMapping) HashMap() else null

    override fun clear() = _map.clear()
    override val build: (C.() -> Unit) -> Int = cBuilder.builder({ c -> add(c) })
    override val buildActive: (C.() -> Unit) -> Int = cBuilder.builder({ c -> add(c, true) })

    private fun add(c: C, activate: Boolean = false): C {
        _map.set(c.index(), c)
        if (nameMapping && !StringUtils.isBlank(c.name()) ) {
            nameMap?.put(c.name(), c)
        }
        if (activate) {
            activate(c.index())
        }
        return c
    }

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

    override fun getId(name: String): Int {
        if (nameMapping) {
            return nameMap?.get(name)?.index() ?:
                    throw RuntimeException("Component: ${cBuilder.typeKey} for name: $name not found")
        }

        val id = _map.get(name)
        return when(id) {
            -1 -> throw RuntimeException("Component: ${cBuilder.typeKey} for name: $name not found")
            else -> id
        }
    }

    private fun <T : NamedComponent> DynArray<T>.get(name: String): Int =
            (0 until this.capacity()).firstOrNull {
                this.array[it] != null && this.array[it].name() == name
            } ?: -1

}