package com.inari.firefly.control.trigger

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent

object TriggerSystem : ComponentSystem {
    override val supportedComponents: IAspects =
        SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(Trigger)

    @JvmField val trigger = ComponentSystem.createComponentMapping(
        Trigger,
        nameMapping = true
    )

    override fun clearSystem() {
        trigger.clear()
    }
}