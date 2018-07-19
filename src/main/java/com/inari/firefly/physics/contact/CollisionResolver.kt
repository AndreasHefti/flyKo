package com.inari.firefly.physics.contact

import com.inari.firefly.entity.Entity
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentType

abstract class CollisionResolver protected constructor() : SystemComponent(CollisionResolver::class.java.name) {

    @JvmField internal var separateDirections = true
    @JvmField internal var yDirectionFirst = true

    abstract fun resolve(entity: Entity)

    override fun componentType() =
        CollisionResolver.Companion

    companion object : SystemComponentType<CollisionResolver>(CollisionResolver::class.java)
}