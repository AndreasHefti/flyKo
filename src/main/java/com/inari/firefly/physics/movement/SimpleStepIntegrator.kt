package com.inari.firefly.physics.movement

import com.inari.firefly.graphics.ETransform

object SimpleStepIntegrator : Integrator {

    override fun integrate(movement: EMovement, transform: ETransform, deltaTimeInSeconds: Float) {
        val velocity = movement.velocity

        if (movement.onGround) {
            if (velocity.dy != 0f )
                velocity.dy = 0f
            return
        }
       velocity.dy =
            velocity.dy + Math.abs( (velocity.dy / movement.mass - 1f ) * 0.2f )
    }

    override fun step(movement: EMovement, transform: ETransform, deltaTimeInSeconds: Float) =
        transform.move(movement.velocity.dx, movement.velocity.dy)
}