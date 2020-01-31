package com.inari.firefly.entity

import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentMap
import com.inari.firefly.control.ControllerSystem
import com.inari.firefly.entity.EntityActivationEvent.Type.ACTIVATED
import com.inari.firefly.entity.EntityActivationEvent.Type.DEACTIVATED
import com.inari.firefly.entity.EntityActivationEvent.send
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.aspect.Aspects

object EntitySystem : ComponentSystem {

    override val supportedComponents: Aspects =
        SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(Entity)

    @JvmField val entities: ComponentMap<Entity> = ComponentSystem.createComponentMapping(
        Entity,
        activationMapping = true,
        nameMapping = true,
        listener = { entity, action -> when (action) {
            ComponentMap.MapAction.ACTIVATED     -> activated(entity)
            ComponentMap.MapAction.DEACTIVATED   -> deactivated(entity)
            else -> {}
        } }
    )

    init {
        FFContext.loadSystem(this)
    }

    operator fun get(entityId: CompId) = entities[entityId.instanceId]
    operator fun get(name: String) = entities[name]
    operator fun get(index: Int) = entities[index]
    operator fun contains(entityId: CompId) = entityId in entities
    operator fun contains(name: String) = name in entities
    operator fun contains(index: Int) = index in entities

    private fun activated(entity: Entity) {
        if (EMeta in entity.components.aspects ) {
            val controllerRef = entity[EMeta].controllerRef
            if (controllerRef >= 0)
                ControllerSystem.register(controllerRef, entity.componentId)
        }

        send(
            entity = entity,
            type = ACTIVATED
        )
    }

    private fun deactivated(entity: Entity) {
        if (EMeta in entity.components.aspects ) {
            val controllerRef = entity[EMeta].controllerRef
            if (controllerRef >= 0)
                ControllerSystem.unregister(controllerRef, entity.componentId)
        }

        send(
            entity = entity,
            type = DEACTIVATED
        )
    }

    override fun clearSystem() = entities.clear()

}