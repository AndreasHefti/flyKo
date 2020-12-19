package com.inari.firefly.graphics.particle

import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.external.TransformData
import com.inari.util.geom.PositionF
import com.inari.util.geom.Vector2f

@ComponentDSL
abstract class Particle protected constructor() {

    @JvmField internal val transformData = TransformData()
    
    val position: PositionF
        get() = transformData.position
    val pivot: PositionF
        get() = transformData.pivot
    val scale: Vector2f
        get() = transformData.scale
    var rotation: Float
        get() = transformData.rotation
        set(value) { transformData.rotation = value }
    var xVelocity: Float  = 0f
    var yVelocity: Float  = 0f
    var mass: Float  = 1f

    interface ParticleBuilder<P : Particle> {
        fun createEmpty(): P
    }

}