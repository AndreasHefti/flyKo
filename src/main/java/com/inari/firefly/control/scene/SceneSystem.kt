package com.inari.firefly.control.scene

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.FFContext
import com.inari.firefly.system.TriggerMap
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent

object SceneSystem : ComponentSystem {


    override val supportedComponents: IAspects =
        SystemComponent.ASPECT_GROUP.createAspects(Scene)

    private val triggerMap = TriggerMap()
    @JvmField val scenes = ComponentSystem.createComponentMapping(
        Scene,
        nameMapping = true
    )

    init {
        FFContext.loadSystem(this)
    }

    override fun clearSystem() {
        scenes.clear()
    }
}