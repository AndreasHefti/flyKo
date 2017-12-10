package com.inari.firefly.control.action

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.FFContext
import com.inari.firefly.system.TriggerMap
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent.Companion.ASPECT_GROUP

object ActionSystem : ComponentSystem {
    override val supportedComponents: IAspects =
        ASPECT_GROUP.createAspects(Action)

    @JvmField internal val triggerMap = TriggerMap()
    @JvmField val actions = ComponentSystem.createComponentMapping(
        Action, nameMapping = true
    )

    init {
        FFContext.loadSystem(this)
    }

    override fun clearSystem() {
        actions.clear()
    }

}