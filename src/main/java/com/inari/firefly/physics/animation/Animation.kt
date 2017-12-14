package com.inari.firefly.physics.animation

import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent

abstract class Animation protected constructor() : SystemComponent() {

    @JvmField internal var looping: Boolean = false

    var ff_Looping: Boolean
        get() = looping
        set(value) { looping = value }

    abstract fun update()
    abstract fun reset()

    override final fun indexedTypeKey() = typeKey
    companion object : ComponentType<Animation> {
        override val typeKey = SystemComponent.createTypeKey(Animation::class.java)
    }
}