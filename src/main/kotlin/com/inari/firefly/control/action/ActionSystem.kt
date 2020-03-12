package com.inari.firefly.control.action

import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.aspect.Aspects

typealias ActionCall = (CompId, CompId, CompId, CompId) -> Unit
object ActionSystem : ComponentSystem {


    override val supportedComponents: Aspects =
            SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(Action)

    @JvmField val actions = ComponentSystem.createComponentMapping(
            Action,
            nameMapping = true
    )

    init {
        FFContext.loadSystem(this)
    }

    override fun clearSystem() {
        actions.clear()
    }

}