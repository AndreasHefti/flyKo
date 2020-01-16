package com.inari.firefly.composite

import com.inari.firefly.FFContext
import com.inari.firefly.component.ComponentMap
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.aspect.Aspects

object CompositeSystem : ComponentSystem {

    override val supportedComponents: Aspects =
            SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(Composite)

    @JvmField val composites = ComponentSystem.createComponentMapping(
            Composite,
            activationMapping = true,
            nameMapping = true,
            listener = { composite, action  -> when (action) {
                ComponentMap.MapAction.CREATED       -> composite.systemLoad()
                ComponentMap.MapAction.ACTIVATED     -> composite.systemActivate()
                ComponentMap.MapAction.DEACTIVATED   -> composite.systemDeactivate()
                ComponentMap.MapAction.DELETED       -> composite.systemUnload()
            } }
    )

    init {
        FFContext.loadSystem(this)
    }

    override fun clearSystem() {
        composites.clear()
    }
}