package com.inari.firefly.physics.animation

import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.property.IFloatPropertyAccessor

class AnimatedFloat private constructor() : AnimatedProperty() {

    @JvmField internal var propertyAccessor: IFloatPropertyAccessor? = null
    @JvmField internal var initValue = 0f

    @JvmField internal var v1 = 0f
    @JvmField internal var v2 = 0f
    @JvmField internal var v3 = 0f

    var ff_InitValue: Float
        get() = initValue
        set(value) { initValue = value }

    override fun init(entity: Entity) {
        propertyAccessor = propertyRef.accessor(entity) as IFloatPropertyAccessor
    }

    override fun reset() {
        v1 = initValue
        v2 = initValue
        v3 = initValue
        propertyAccessor?.set(initValue)
    }

    companion object : Builder<AnimatedFloat>() {
        override fun createEmpty(): AnimatedFloat =
            AnimatedFloat()
    }
}