package com.inari.firefly.physics.animation.easing

import com.inari.commons.geom.Easing
import com.inari.firefly.NO_PROPERTY_REF
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.property.FloatPropertyAccessor
import com.inari.firefly.physics.animation.AnimatedProperty

class EasingAnimated private constructor() : AnimatedProperty() {

    @JvmField internal var propertyAccessor: FloatPropertyAccessor? = null
    @JvmField internal var easingType: Easing.Type = Easing.Type.LINEAR
    @JvmField internal var startValue = 0f
    @JvmField internal var endValue = 0f
    @JvmField internal var duration: Long = 0
    @JvmField internal var inverseOnLoop = false

    @JvmField internal var inverse = false
    @JvmField internal var changeInValue = 0f
    @JvmField internal var runningTime: Long = 0

    var ff_EasingType: Easing.Type
        get() = easingType
        set(value) { easingType = value }
    var ff_StartValue: Float
        get() = startValue
        set(value) { startValue = value }
    var ff_EndValue: Float
        get() = endValue
        set(value) { endValue = value }
    var ff_Duration: Long
        get() = duration
        set(value) { duration = value }
    var ff_InverseOnLoop: Boolean
        get() = inverseOnLoop
        set(value) { inverseOnLoop = value }

    override fun init(entity: Entity) {
        if (propertyRef == NO_PROPERTY_REF)
            throw IllegalStateException("No property reference for animation is set")
        propertyAccessor = propertyRef.accessor(entity) as FloatPropertyAccessor
        if (animationRef < 0)
            animationRef = EasingAnimation
                .activate()
                .index()
        reset()
    }

    override fun reset() {
        runningTime = 0
        changeInValue  = endValue - startValue
        if (changeInValue < 0) {
            inverse = true
            changeInValue = Math.abs(changeInValue)
        } else {
            inverse = false
        }
        propertyAccessor?.set(startValue)
    }

    companion object : Builder<EasingAnimated>() {
        override fun createEmpty(): EasingAnimated =
            EasingAnimated()
    }

}