package com.inari.firefly.entity

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.component.mapping.ComponentMap
import com.inari.firefly.system.FFContext
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent

object EntitySystem : ComponentSystem {

    override val supportedComponents: IAspects = SystemComponent.ASPECT_GROUP.createAspects(
        Entity.typeKey
    )

    private val entities: ComponentMap<Entity> = ComponentMap(Entity)

    init {
        FFContext.registerComponentMapper(entities)
    }

    override fun clearSystem() = entities.clear()
    override fun dispose() {
        FFContext.disposeComponentMapper(entities)
    }
}