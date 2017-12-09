package com.inari.firefly.physics.animation

import com.inari.commons.lang.aspect.Aspects
import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.Expr
import com.inari.firefly.FFApp.UpdateEvent
import com.inari.firefly.FFContext
import com.inari.firefly.control.ControllerSystem
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityActivationEvent
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent.Companion.ASPECT_GROUP

object AnimationSystem : ComponentSystem {
    override val supportedComponents: IAspects =
        ASPECT_GROUP.createAspects(Animation)

    @JvmField val animations = ComponentSystem.createComponentMapping(
        Animation,
        nameMapping = true,
        activationMapping = true
    )

    init {
        val update: Expr<Animation> = { animation -> animation() }

        FFContext.registerListener(UpdateEvent, object : UpdateEvent.Listener {
            override fun invoke() =
                animations.forEachActive(update)
        })

        FFContext.registerListener(EntityActivationEvent, object: EntityActivationEvent.Listener {
            override fun entityActivated(entity: Entity) =
                activateForEntity(entity)
            override fun entityDeactivated(entity: Entity) =
                deactivateForEntity(entity)
            override fun match(aspects: Aspects): Boolean =
                EAnimation in aspects
        })

        FFContext.loadSystem(this)
    }

    private fun activateForEntity(entity: Entity) {
        val eAnim = entity[EAnimation]
        val i = 0
        while (i < eAnim.animations.capacity()) {
            val animProp = eAnim.animations[i] ?: continue
            animProp.compile(entity)
            animations[animProp.animationRef].register(animProp)
        }

        if (eAnim.controllerRef >= 0) {
            ControllerSystem.controller[eAnim.controllerRef]
                .register(entity.componentId)
        }
    }

    private fun deactivateForEntity(entity: Entity) {
        val eAnim = entity[EAnimation]
        val i = 0
        while (i < eAnim.animations.capacity()) {
            val animProp = eAnim.animations[i] ?: continue
            animations[animProp.animationRef].dispose(animProp)
        }

        if (eAnim.controllerRef >= 0 && eAnim.controllerRef in ControllerSystem.controller) {
            ControllerSystem.controller[eAnim.controllerRef]
                .unregister(entity.componentId)
        }
    }


    override fun clearSystem() =
        animations.clear()
}