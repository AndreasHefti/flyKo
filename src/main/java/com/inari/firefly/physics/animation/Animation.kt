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

    override fun componentType(): ComponentType<Animation> =
        Animation.Companion

    companion object : ComponentType<Animation> {
        override val indexedTypeKey by lazy { TypeKeyBuilder.create(Animation::class.java) }
    }
}

interface FloatAnimation {
    val value: Float
}

interface IntAnimation {
    val value: Int
}

interface ValueAnimation<out T> {
    val value: T
}