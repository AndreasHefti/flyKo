package com.inari.firefly.physics.movement

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.FFApp.UpdateEvent
import com.inari.firefly.FFContext
import com.inari.firefly.control.ControllerSystem
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityActivationEvent
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.system.FFSystem
import java.util.*

object MovementSystem : FFSystem {

    var movementIntegrator: Integrator = SimpleStepIntegrator

    private val entities: BitSet = BitSet()

    init {
        FFContext.registerListener(UpdateEvent, object : UpdateEvent.Listener {
            override fun invoke() {
                MoveEvent.entities.clear()
                val deltaTimeInSeconds = FFContext.timer.timeElapsed / 1000f

                var i: Int = entities.nextSetBit(0)
                while (i >= 0) {
                    val entity = EntitySystem[i]
                    i = entities.nextSetBit(i + 1)
                    if (!entity.has(EMovement))
                        continue

                    val movement = entity[EMovement]
                    if (!movement.active || !movement.scheduler.needsUpdate())
                        continue

                    val transform = entity[ETransform]
                    if ( movement.velocity.dx != 0f || movement.velocity.dy != 0f ) {
                        movementIntegrator.step( movement, transform, deltaTimeInSeconds )
                        MoveEvent.entities.set(entity.index())
                    }

                    movementIntegrator.integrate( movement, transform, deltaTimeInSeconds )
                }

                if (!MoveEvent.entities.isEmpty)
                    FFContext.notify(MoveEvent)
            }
        })

        FFContext.registerListener(EntityActivationEvent, object : EntityActivationEvent.Listener {
            override fun entityActivated(entity: Entity) {
                entities.set(entity.index())
                val movement = entity[EMovement]
                if (movement.controllerRef >= 0) {
                    ControllerSystem.register(movement.controllerRef, entity.componentId)
                }
            }
            override fun entityDeactivated(entity: Entity) {
                entities.set(entity.index(), false)
                val movement = entity[EMovement]
                if (movement.controllerRef >= 0) {
                    ControllerSystem.unregister(movement.controllerRef, entity.componentId)
                }
            }
            override fun match(aspects: IAspects): Boolean =
                aspects.contains(EMovement)
        })
    }

    override fun clearSystem() {}
}