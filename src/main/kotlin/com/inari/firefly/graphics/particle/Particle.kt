package com.inari.firefly.graphics.particle

import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.external.TransformData
import com.inari.util.geom.PositionF
import com.inari.util.geom.Vector2f

@ComponentDSL
abstract class Particle protected constructor() {

    @JvmField internal val transformData = TransformData()
    @JvmField internal var xVelocity: Float = 0f
    @JvmField internal var yVelocity: Float = 0f
    @JvmField internal var mass: Float = 1f

    val ff_Position: PositionF
        get() = transformData.position
    val ff_Pivot: PositionF
        get() = transformData.pivot
    val ff_Scale: Vector2f
        get() = transformData.scale
    var ff_Rotation: Float
        get() = transformData.rotation
        set(value) { transformData.rotation = value }
    var ff_XVelocity: Float
        get() = xVelocity
        set(value) {xVelocity = value}
    var ff_YVelocity: Float
        get() = yVelocity
        set(value) {yVelocity = value}
    var ff_Mass: Float
        get() = mass
        set(value) {mass = value}

    interface ParticleBuilder<P : Particle> {
        fun createEmpty(): P
    }

}