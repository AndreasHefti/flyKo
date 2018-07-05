package com.inari.firefly.physics.contact

import com.inari.firefly.component.ComponentType
import com.inari.firefly.entity.Entity
import com.inari.firefly.system.component.SystemComponent

abstract class CollisionResolver protected constructor() : SystemComponent() {

    @JvmField internal var separateDirections = true
    @JvmField internal var yDirectionFirst = true

    abstract fun resolve(entity: Entity)

    override fun componentType(): ComponentType<CollisionResolver> =
        CollisionResolver.Companion

    companion object : ComponentType<CollisionResolver> {
        override val indexedTypeKey by lazy { TypeKeyBuilder.create(CollisionResolver::class.java) }
    }
}