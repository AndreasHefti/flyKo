package com.inari.firefly.physics.movement

import com.inari.firefly.FFContext
import com.inari.firefly.INFINITE_SCHEDULER
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.Controller
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.firefly.external.FFTimer
import com.inari.util.geom.Vector2f

class EMovement private constructor() : EntityComponent(EMovement::class.java.name) {

    @JvmField internal var controllerRef = -1
    @JvmField internal var scheduler: FFTimer.Scheduler = INFINITE_SCHEDULER

    var active: Boolean = true
    var velocity: Vector2f  = Vector2f(0f, 0f)
    var velocityX: Float
        get() = velocity.dx
        set(value) { velocity.dx = value }
    var velocityY: Float
        get() = velocity.dy
        set(value) { velocity.dy = value }
    var acceleration: Vector2f = Vector2f(0f, 0f)
    var accelerationX: Float
        get() = acceleration.dx
        set(value) { acceleration.dx = value }
    var accelerationY: Float
        get() = acceleration.dy
        set(value) { acceleration.dy = value }
    var mass: Float = 0f
    var massFactor: Float  = 1f
    var maxGravityVelocity: Float = 1f
    var onGround: Boolean  = false
    var updateResolution: Float
        get() = throw UnsupportedOperationException()
        set(value) { scheduler = FFContext.timer.createUpdateScheduler(value) }
    val controller = ComponentRefResolver(Controller) { index -> controllerRef = index }

    override fun reset() {
        active = false
        velocity.dx = 0f
        velocity.dy = 0f
        acceleration.dx = 0f
        acceleration.dy = 0f
        mass = 0f
        massFactor = 1f
        maxGravityVelocity = 200f
        onGround = false
        scheduler = INFINITE_SCHEDULER
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<EMovement>(EMovement::class.java) {
        override fun createEmpty() = EMovement()
    }
}