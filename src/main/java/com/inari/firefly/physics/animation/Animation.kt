package com.inari.firefly.physics.animation

import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent

abstract class Animation protected constructor() : SystemComponent() {

    abstract operator fun invoke()

    abstract fun register(animated: AnimatedProperty)
    abstract fun dispose(animated: AnimatedProperty)

    protected fun wrongAnimatedType(expected: String, actual: String) {
        throw IllegalArgumentException("Animated Type Mismatch. Expected: $expected Actual: $actual")
    }

    override final fun indexedTypeKey() = Animation.typeKey
    companion object : ComponentType<Animation> {
        override val typeKey = SystemComponent.createTypeKey(Animation::class.java)
    }
}