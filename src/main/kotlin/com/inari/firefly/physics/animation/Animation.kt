package com.inari.firefly.physics.animation

import com.inari.firefly.NULL_CALL
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentType
import com.inari.util.Call

abstract class Animation protected constructor() : SystemComponent(Animation::class.java.name) {

    @JvmField internal var looping: Boolean = false
    @JvmField internal var callback: Call = NULL_CALL

    var ff_Looping: Boolean
        get() = looping
        set(value) { looping = value }

    var ff_Callback: Call
        get() = callback
        set(value) { callback = value }

    abstract fun update()
    abstract fun reset()

    override fun componentType(): ComponentType<Animation> = Companion
    companion object : SystemComponentType<Animation>(Animation::class.java)
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