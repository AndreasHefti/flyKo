package com.inari.firefly.control

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent.Companion.SYSTEM_COMPONENT_ASPECTS

object ControllerSystem : ComponentSystem {

    override val supportedComponents: IAspects =
        SYSTEM_COMPONENT_ASPECTS.createAspects(Controller.typeKey)

    @JvmField val controller = ComponentSystem.createComponentMapping(
        Controller,
        activationMapping = true,
        nameMapping = true
    )

    init {
        FFContext.registerListener(
            FFApp.UpdateEvent,
            object : FFApp.UpdateEvent.Listener {
                override fun invoke() {
                    controller.forEachActive({ c ->
                        if (c.needsUpdate)
                            c.update()
                    })
                }
            }
        )

        FFContext.loadSystem(this)
    }

    fun register(controllerName: String, id: CompId) =
        register(controller.idForName(controllerName), id)

    fun register(controllerId: CompId, id: CompId) =
        register(controllerId.index, id)

    fun register(controllerId: Int, id: CompId) {
        if (controllerId in controller)
            controller[controllerId].register(id)
    }

    fun unregister(controllerName: String, id: CompId) =
        unregister(controller.idForName(controllerName), id)

    fun unregister(controllerId: CompId, id: CompId) =
        unregister(controllerId.index, id)

    fun unregister(controllerId: Int, id: CompId) {
        if (controllerId in controller)
            controller[controllerId].unregister(id)
    }

    override fun clearSystem() {
        controller.clear()
    }
}