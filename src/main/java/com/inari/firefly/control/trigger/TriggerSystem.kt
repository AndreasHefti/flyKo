package com.inari.firefly.control.trigger

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent

object TriggerSystem : ComponentSystem {

    override val supportedComponents: IAspects =
        SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(Trigger)

    @JvmField val triggers = ComponentSystem.createComponentMapping(
        Trigger,
        nameMapping = true
    )

    init {
        FFContext.registerListener(FFApp.UpdateEvent, object : FFApp.UpdateEvent.Listener {
            override fun invoke() {
                var i = 0
                while (i < triggers.map.capacity()) {
                    val trigger = triggers.map[i++] ?: continue
                    if (trigger.condition()) {
                        trigger.call()
                        if (trigger.removeAfter) {
                            triggers.delete(trigger.index())
                        }
                    }
                }
            }
        })

        FFContext.loadSystem(this)
    }

    override fun clearSystem() {
        triggers.clear()
    }
}