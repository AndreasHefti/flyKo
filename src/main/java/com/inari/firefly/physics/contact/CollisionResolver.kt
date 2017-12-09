package com.inari.firefly.physics.contact

import com.inari.firefly.component.ComponentType
import com.inari.firefly.entity.Entity
import com.inari.firefly.system.component.SystemComponent

abstract class CollisionResolver protected constructor() : SystemComponent() {

    @JvmField internal var separateDirections = true
    @JvmField internal var yDirectionFirst = true

    abstract fun resolve(entity: Entity)

    override final fun indexedTypeKey() = typeKey
    companion object : ComponentType<CollisionResolver> {
        override val typeKey = SystemComponent.createTypeKey(CollisionResolver::class.java)
    }
}