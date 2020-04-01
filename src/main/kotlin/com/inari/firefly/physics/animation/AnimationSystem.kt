package com.inari.firefly.physics.animation

import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityActivationEvent
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.firefly.physics.animation.entity.EntityPropertyAnimation
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
        FFContext.registerListener(FFApp.UpdateEvent) {
                animations.forEachActive { it.update() }
        }

        FFContext.registerListener(EntityActivationEvent, object: EntityActivationEvent.Listener {
            override fun entityActivated(entity: Entity) =
                registerEntityAnimations(entity)
            override fun entityDeactivated(entity: Entity) =
                detachEntityAnimations(entity)
            override fun match(aspects: Aspects): Boolean =
                EAnimation in aspects
        })

        FFContext.loadSystem(this)
    }

    fun registerEntityAnimations(entity: Entity) {
        val eAnim = entity[EAnimation]
        var i = eAnim.animations.nextSetBit(0)
        while (i >= 0) {
            val animProp: EntityPropertyAnimation = animations.getAs(i)
            animProp.applyToEntity(entity)
            i = eAnim.animations.nextSetBit(i + 1)
        }
    }

    fun detachEntityAnimations(entity: Entity) {
        val eAnim = entity[EAnimation]
        var i = eAnim.animations.nextSetBit(0)
        while (i >= 0) {
            val animProp: EntityPropertyAnimation = animations.getAs(i)
            animProp.detachFromEntity(entity)
            i = eAnim.animations.nextSetBit(i + 1)
        }
    }

    override fun clearSystem() =
        animations.clear()
}