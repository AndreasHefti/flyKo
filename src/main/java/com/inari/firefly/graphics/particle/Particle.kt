package com.inari.firefly.graphics.particle

import com.inari.firefly.external.TransformData

abstract class Particle(
    @JvmField internal var x: Float,
    @JvmField internal var y: Float = 0f,
    @JvmField internal var xScale: Float = 1f,
    @JvmField internal var yScale: Float = 1f,
    @JvmField internal var xPivot: Float = 0f,
    @JvmField internal var yPivot: Float = 0f,
    @JvmField internal var rot: Float = 0f,
    @JvmField internal var xVelocity: Float = 0f,
    @JvmField internal var yVelocity: Float = 0f,
    @JvmField internal var mass: Float = 1f
) : TransformData {

    override val xOffset: Float get() = x
    override val yOffset: Float get() = y
    override val scaleX: Float get() = xScale
    override val scaleY: Float get() = yScale
    override val pivotX: Float get() = xPivot
    override val pivotY: Float get()  = yPivot
    override val rotation: Float get() = rot
    override val hasRotation: Boolean get() = rot != 0f
    override val hasScale: Boolean get() = xScale != 1f || yScale != 1f
}