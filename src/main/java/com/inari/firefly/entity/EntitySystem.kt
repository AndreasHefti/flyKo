package com.inari.firefly.entity

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.component.IComponentMap
import com.inari.firefly.component.MapListener
import com.inari.firefly.control.Controller
import com.inari.firefly.control.ControllerSystem
import com.inari.firefly.control.PolyController
import com.inari.firefly.entity.EntityActivationEvent.Type.ACTIVATED
import com.inari.firefly.entity.EntityActivationEvent.Type.DEACTIVATED
import com.inari.firefly.entity.EntityActivationEvent.send
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent

object EntitySystem : ComponentSystem {

    override val supportedComponents: IAspects= SystemComponent.ASPECT_GROUP.createAspects(
        Entity.typeKey
    )

    val entities: IComponentMap<Entity>

    init {
        val mapListener: MapListener<Entity> = {
            entity, action -> when (action) {
                IComponentMap.MapAction.ACTIVATED     -> activated(entity)
                IComponentMap.MapAction.DEACTIVATED   -> deactivated(entity)
                else -> {}
            }
        }
        entities = ComponentSystem.createComponentMapping(Entity)

    }

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
        val entityIndex = entity.index()
        val exp: (PolyController) -> Unit =
            if (activated) { controller: PolyController -> controller.register(entityIndex) }
            else { controller -> controller.unregister(entityIndex) }

        ControllerSystem.controller.forEachSubtypeIn(
            entity.get(EMeta).ff_Controller,
            exp
        )
    }

    override fun clearSystem() = entities.clear()

}