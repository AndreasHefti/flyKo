package com.inari.firefly.control.trigger

import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.aspect.Aspects

object TriggerSystem : ComponentSystem {
    override val supportedComponents: Aspects =
        SystemComponent.ASPECT_GROUP.createAspects(Trigger)

    @JvmField val trigger = ComponentSystem.createComponentMapping(
        Trigger,
        nameMapping = true
    )

    override fun clearSystem() {
        trigger.clear()
    }
}