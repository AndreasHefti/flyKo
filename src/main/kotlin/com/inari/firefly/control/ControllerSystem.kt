package com.inari.firefly.control

import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityActivationEvent
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.java.types.BitSet
import com.inari.util.aspect.Aspects

object ControllerSystem : ComponentSystem {

    override val supportedComponents: Aspects = SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(
        SystemComponentController,
        EntityController
    )

    @JvmField val systemComponentController = ComponentSystem.createComponentMapping(
        SystemComponentController,
        activationMapping = true,
        nameMapping = true
    )

    @JvmField val entityController = ComponentSystem.createComponentMapping(
        EntityController,
        activationMapping = true,
    )

    private val entities: BitSet = BitSet()

    init {
        FFContext.registerListener(FFApp.UpdateEvent) {
            systemComponentController.forEachActive{
                    c -> c.update()
            }

            var i: Int = entities.nextSetBit(0)
            while (i >= 0) {
                i = entities.nextSetBit(i + 1)
                val cId = FFContext[i, EControl].controllerRef
                if (entityController.isActive(cId))
                    entityController[cId].update(i)
            }
        }

        FFContext.registerListener(EntityActivationEvent, object : EntityActivationEvent.Listener {
            override fun entityActivated(entity: Entity) {
                entities.set(entity.index)
            }
            override fun entityDeactivated(entity: Entity) {
                entities.set(entity.index, false)
            }
            override fun match(aspects: Aspects): Boolean =
                aspects.contains(EControl)
        })

        FFContext.loadSystem(this)
    }

    override fun clearSystem() {
        systemComponentController.clear()
        entityController.clear()
        entities.clear()
    }
}