package com.inari.firefly.graphics.particle

import com.inari.firefly.external.TransformData

abstract class Particle(
    @JvmField internal var xVelocity: Float = 0f,
    @JvmField internal var yVelocity: Float = 0f,
    @JvmField internal var mass: Float = 1f
) {
    @JvmField val transformData = TransformData()
}