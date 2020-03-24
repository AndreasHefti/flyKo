package com.inari.firefly.physics.animation.entity

import com.inari.firefly.FFContext
import com.inari.firefly.NO_PROPERTY_REF
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.property.VirtualPropertyRef
import com.inari.firefly.physics.animation.Animation

abstract class EntityPropertyAnimation protected constructor() : Animation() {

    @JvmField protected var propertyRef: VirtualPropertyRef = NO_PROPERTY_REF

    var ff_PropertyRef: VirtualPropertyRef
        get() = propertyRef
        set(value) { propertyRef = if (FFContext.isActive(componentId)) throw IllegalStateException() else value }

    internal abstract fun applyToEntity(entity: Entity)
    internal abstract fun detachFromEntity(entity: Entity)

}