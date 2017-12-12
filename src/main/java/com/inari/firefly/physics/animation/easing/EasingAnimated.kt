package com.inari.firefly.physics.animation.easing

import com.inari.commons.geom.Easing
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.property.IFloatPropertyAccessor
import com.inari.firefly.physics.animation.AnimatedProperty

class EasingAnimated private constructor() : AnimatedProperty() {

    @JvmField internal var propertyAccessor: IFloatPropertyAccessor? = null
    @JvmField internal var easingType: Easing.Type = Easing.Type.LINEAR
    @JvmField internal var startValue = 0f
    @JvmField internal var endValue = 0f
    @JvmField internal var duration: Long = 0
    @JvmField internal var inverseOnLoop = false

    @JvmField internal var runningTime: Long = -1

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
        propertyAccessor = propertyRef.accessor(entity) as IFloatPropertyAccessor
    }

    override fun reset() {
        runningTime = FFContext.timer.time
        propertyAccessor?.set(startValue)
    }

    companion object : Builder<EasingAnimated>() {
        override fun createEmpty(): EasingAnimated =
            EasingAnimated()
    }

}