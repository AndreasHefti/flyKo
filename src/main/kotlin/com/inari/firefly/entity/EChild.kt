package com.inari.firefly.entity

import com.inari.firefly.component.ComponentRefResolver

class EChild private constructor () : EntityComponent(EChild::class.java.name) {

    @JvmField internal var int_parent: Int = -1

    var parent = ComponentRefResolver(Entity) { index -> int_parent = index }
    var zPos: Int = -1

    override fun reset() {
        int_parent = -1
        zPos = -1
    }

    override fun componentType() =  Companion
    companion object : EntityComponentType<EChild>(EChild::class.java) {
        override fun createEmpty() = EChild()
    }
}