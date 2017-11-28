package com.inari.firefly.control

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.component.IComponentMap
import com.inari.firefly.external.FFTimer
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent

object ControllerSystem : ComponentSystem {

    override val supportedComponents: IAspects = SystemComponent.ASPECT_GROUP.createAspects(
        Controller.typeKey
    )

    val controller: IComponentMap<Controller> = ComponentSystem.createComponentMapping(
        Controller,
        activationMapping = true,
        nameMapping = true
    )

    init {
        FFContext.registerListener(
            FFApp.UpdateEvent,
            { _: FFTimer -> controller.forEachActive { controller -> controller.processUpdate() } }
        )
    }

    override fun clearSystem() {
        controller.clear()
    }
}