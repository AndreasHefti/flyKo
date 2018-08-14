package com.inari.firefly.physics.animation

import com.inari.firefly.Consumer
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.control.ControllerSystem
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.firefly.physics.animation.entity.EntityPropertyAnimation
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityActivationEvent
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.aspect.Aspects

object AnimationSystem : ComponentSystem {
    override val supportedComponents: Aspects =
        SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(Animation)

    @JvmField val animations = ComponentSystem.createComponentMapping(
        Animation,
        nameMapping = true,
        activationMapping = true
    )

    init {
        val update: Consumer<Animation> = { animation -> animation.update() }

        FFContext.registerListener(FFApp.UpdateEvent, object : FFApp.UpdateEvent.Listener {
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
        var i = eAnim.animations.nextSetBit(0)
        while (i >= 0) {
            val animProp: EntityPropertyAnimation = animations.getAs(i)
            animProp.compile(entity)
            if (eAnim.activeAnimations[i])
                animations.activate(i)
            i = eAnim.animations.nextSetBit(i + 1)
        }

        if (eAnim.controllerRef >= 0) {
            ControllerSystem.controller[eAnim.controllerRef]
                .register(entity.componentId)
        }
    }

    private fun deactivateForEntity(entity: Entity) {
        val eAnim = entity[EAnimation]
        var i = eAnim.animations.nextSetBit(0)
        while (i >= 0) {
            animations.getAs<EntityPropertyAnimation>(i).reset()
            animations.deactivate(i)
            i = eAnim.animations.nextSetBit(i + 1)
        }

        if (eAnim.controllerRef >= 0 && eAnim.controllerRef in ControllerSystem.controller) {
            ControllerSystem.controller[eAnim.controllerRef]
                .unregister(entity.componentId)
        }
    }

    override fun clearSystem() =
        animations.clear()
}