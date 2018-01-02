package com.inari.firefly.physics.movement

import com.inari.firefly.graphics.ETransform

object EulerIntegrator : Integrator {

    var gravity = 9.8f
    var shift = Math.pow(10.0, 0.0).toFloat()
    var scale = -1
        set(value) {
            field = value
            shift = if (value < 0)
                Math.pow(10.0, 0.0).toFloat()
            else
                Math.pow(10.0, value.toDouble()).toFloat()
        }

    override fun integrate(movement: EMovement, transform: ETransform, deltaTimeInSeconds: Float) {
        gravityIntegration( movement )
        val velocity = movement.velocity
        val acceleration = movement.acceleration

        velocity.dx = velocity.dx + acceleration.dx * deltaTimeInSeconds
        velocity.dy = velocity.dy + acceleration.dy * deltaTimeInSeconds
    }

    override fun step(movement: EMovement, transform: ETransform, deltaTimeInSeconds: Float) {
        transform.move(
            Math.round( movement.velocity.dx * deltaTimeInSeconds * shift ) / shift,
            Math.round( movement.velocity.dy * deltaTimeInSeconds * shift ) / shift
        )
    }

    private fun gravityIntegration(movement: EMovement) {
        if (movement.onGround) {
            movement.velocity.dy = 0f
            movement.acceleration.dy = 0f
        } else {
            if (movement.velocity.dy >= movement.maxGravityVelocity) {
                movement.acceleration.dy = 0f
                movement.velocity.dy = movement.maxGravityVelocity
                return
            }
            movement.acceleration.dy = gravity * (movement.mass * movement.massFactor)
        }
    }
}