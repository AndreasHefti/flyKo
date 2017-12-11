package com.inari.firefly.entity

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentMap
import com.inari.firefly.control.Controller
import com.inari.firefly.control.ControllerSystem
import com.inari.firefly.entity.EntityActivationEvent.Type.ACTIVATED
import com.inari.firefly.entity.EntityActivationEvent.Type.DEACTIVATED
import com.inari.firefly.entity.EntityActivationEvent.send
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent.Companion.SYSTEM_COMPONENT_ASPECTS

object EntitySystem : ComponentSystem {

    override val supportedComponents: IAspects = SYSTEM_COMPONENT_ASPECTS.createAspects(
        Entity.typeKey
    )

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

    operator fun get(entityId: CompId) = entities[entityId.index]
    operator fun get(name: String) = entities[name]
    operator fun get(index: Int) = entities[index]
    operator fun contains(entityId: CompId) = entityId in entities
    operator fun contains(name: String) = name in entities
    operator fun contains(index: Int) = index in entities

    private fun activated(entity: Entity) {
        if (entity.has(EMeta)) {
            notifyEntityController(entity, true)
        }

        send(
            entity = entity,
            type = ACTIVATED
        )
    }

    private fun deactivated(entity: Entity) {
        if (entity.has(EMeta)) {
            notifyEntityController(entity, false)
        }

        send(
            entity = entity,
            type = DEACTIVATED
        )
    }

    private fun notifyEntityController(entity: Entity, activated: Boolean) {
        val expr: (Controller) -> Unit =
            if (activated) { controller: Controller -> controller.register(entity.componentId) }
            else { controller -> controller.unregister(entity.componentId) }

        ControllerSystem.controller.forEachIn(
            entity[EMeta].controller,
            expr
        )
    }

    override fun clearSystem() = entities.clear()

}