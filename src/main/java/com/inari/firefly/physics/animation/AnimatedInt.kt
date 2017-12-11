package com.inari.firefly.physics.animation

import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.property.IIntPropertyAccessor

class AnimatedInt private constructor() : AnimatedProperty() {

    @JvmField internal var propertyAccessor: IIntPropertyAccessor = null!!
    @JvmField internal var initValue = 0

    @JvmField internal var v1 = 0
    @JvmField internal var v2 = 0
    @JvmField internal var v3 = 0

    var ff_InitValue: Int
        get() = initValue
        set(value) { initValue = value }

    override fun init(entity: Entity) {
        propertyAccessor = propertyRef.accessor(entity) as IIntPropertyAccessor
    }

    override fun reset() {
        v1 = initValue
        v2 = initValue
        v3 = initValue
    }

    companion object : Builder<AnimatedInt>() {
        override fun createEmpty(): AnimatedInt =
            AnimatedInt()
    }
}