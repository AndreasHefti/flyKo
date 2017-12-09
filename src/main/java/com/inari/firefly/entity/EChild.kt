package com.inari.firefly.entity

import com.inari.firefly.component.CompId

class EChild private constructor () : EntityComponent() {

    @JvmField internal var parent: Int = -1
    @JvmField internal var zpos: Int = -1

    var ff_Parent: Int
        get() = parent
        set(value) { parent = value }
    var ff_ZPos: Int
        get() = zpos
        set(value) { zpos = value }

    fun ff_setParent(id: CompId) = { ff_Parent = id.index }
    fun ff_setParent(name: String) = { ff_Parent = EntitySystem[name].index() }

    override fun indexedTypeKey() = EChild.typeKey
    override fun reset() {
        parent = -1
        zpos = -1
    }

    override fun toString(): String =
        "EChild(parent=$parent, zpos=$zpos)"

    companion object : EntityComponentType<EChild>() {
        override val typeKey = EntityComponent.createTypeKey(EChild::class.java)
        override fun createEmpty() = EChild()
    }
}