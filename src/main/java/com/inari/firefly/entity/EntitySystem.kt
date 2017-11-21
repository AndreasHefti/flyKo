package com.inari.firefly.entity

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.component.ComponentMap
import com.inari.firefly.component.ComponentType
import com.inari.firefly.component.IComponentMap
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent

object EntitySystem : ComponentSystem {
    override val supportedComponents: IAspects = SystemComponent.ASPECT_GROUP.createAspects(
        Entity.typeKey
    )

    val entities: IComponentMap<Entity> = ComponentMap(cBuilder = Entity)
    val build = entities.build

    @Suppress("UNCHECKED_CAST")
    override fun <C : SystemComponent> with(type: ComponentType<C>): IComponentMap<C> = when (type) {
        Entity -> entities as IComponentMap<C>
        else -> throw RuntimeException("Component of type ${type.typeKey} is not supported")
    }
    override fun clearSystem() = entities.clear()
}