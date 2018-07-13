package com.inari.firefly.control.action

import com.inari.firefly.FFContext
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.aspect.Aspects

object ActionSystem : ComponentSystem {
    override val supportedComponents: Aspects =
        SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(Action)

    @JvmField val actions = ComponentSystem.createComponentMapping(
        Action, nameMapping = true
    )

    operator fun invoke(index: Int, entityIndex: Int) {
        if (index !in actions || entityIndex !in EntitySystem)
            return

        actions[index].entityAction(EntitySystem[entityIndex])
    }

    init {
        FFContext.loadSystem(this)
    }

    override fun clearSystem() {
        actions.clear()
    }

}