package com.inari.firefly.physics.animation.easing

import com.inari.firefly.entity.property.FloatPropertyAccessor
import com.inari.firefly.physics.animation.Animation
import com.inari.firefly.physics.animation.FloatAnimation
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.geom.Easing

class EasedValue : Animation(), FloatAnimation {

    private var control = EasingControl(this)


    var ff_Easing: Easing.EasingFunctions.EasingFunction
        get() = control.easing
        set(value) { control.easing = value }
    var ff_StartValue: Float
        get() = control.startValue
        set(value) { control.startValue = value }
    var ff_EndValue: Float
        get() = control.endValue
        set(value) { control.endValue = value }
    var ff_Duration: Long
        get() = control.duration
        set(value) { control.duration = value }
    var ff_InverseOnLoop: Boolean
        get() = control.inverseOnLoop
        set(value) { control.inverseOnLoop = value }
    var ff_PropertyAccessor: FloatPropertyAccessor
        get() = control.propertyAccessor
        set(value) { control.propertyAccessor = value }

    override val value: Float
        get() = control.propertyAccessor.get()

    override fun reset() = control.reset()
    override fun update() = control.update()

    companion object : SystemComponentSubType<Animation, EasedValue>(Animation, EasedValue::class.java) {
        override fun createEmpty() = EasedValue()
    }
}