package com.inari.firefly.entity

import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.component.ComponentType

class EChild private constructor () : EntityComponent(EChild::class.java.name) {

    @JvmField internal var parent: Int = -1
    @JvmField internal var zpos: Int = -1

    var ff_Parent = ComponentRefResolver(Entity) { index -> parent = index }
    var ff_ZPos: Int
        get() = zpos
        set(value) { zpos = value }

    override fun reset() {
        parent = -1
        zpos = -1
    }

    override fun componentType(): ComponentType<EChild> =
        EChild.Companion

    override fun toString(): String =
        "EChild(parent=$parent, zpos=$zpos)"

    companion object : EntityComponentType<EChild>(EChild::class.java) {
        override fun createEmpty() = EChild()
    }
}