package com.inari.firefly.control

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent.Companion.ASPECT_GROUP

object ControllerSystem : ComponentSystem {

    override val supportedComponents: IAspects =
        ASPECT_GROUP.createAspects(Controller.typeKey)

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
                    controller.forEachActive({ controller ->
                        if (controller.needsUpdate)
                            controller.update()
                    })
                }
            }
        )

        FFContext.loadSystem(this)
    }

    override fun clearSystem() {
        controller.clear()
    }
}