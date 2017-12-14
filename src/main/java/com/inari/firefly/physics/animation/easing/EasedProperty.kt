package com.inari.firefly.physics.animation.easing

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.firefly.FFContext
import com.inari.firefly.NO_PROPERTY_REF
import com.inari.firefly.physics.animation.Animation
import com.inari.firefly.physics.animation.AnimationSystem
import com.inari.firefly.physics.animation.FloatAnimation
import com.inari.firefly.physics.animation.entity.EntityPropertyAnimation
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.property.FloatPropertyAccessor
import com.inari.firefly.system.component.ISubType

class EasedProperty : EntityPropertyAnimation(), FloatAnimation {

    private var propertyAccessor: FloatPropertyAccessor? = null
    private var data = EasingData()

    var ff_Easing: Easing.EasingFunction
        get() = data.easing
        set(value) { data.easing = value }
    var ff_StartValue: Float
        get() = data.startValue
        set(value) { data.startValue = value }
    var ff_EndValue: Float
        get() = data.endValue
        set(value) { data.endValue = value }
    var ff_Duration: Long
        get() = data.duration
        set(value) { data.duration = value }
    var ff_InverseOnLoop: Boolean
        get() = data.inverseOnLoop
        set(value) { data.inverseOnLoop = value }

    override val value: Float
        get() = propertyAccessor?.get() ?: -0f

    override fun init(entity: Entity) {
        if (propertyRef == NO_PROPERTY_REF)
            throw IllegalStateException("No property reference for animation is set")
        propertyAccessor = propertyRef.accessor(entity) as FloatPropertyAccessor
        reset()
    }

    override fun reset() {
        data.runningTime = 0
        data.changeInValue  = data.endValue - data.startValue
        if (data.changeInValue < 0) {
            data.inverse = true
            data.changeInValue = Math.abs(data.changeInValue)
        } else {
            data.inverse = false
        }
        propertyAccessor?.set(data.startValue)
    }

    override fun update() {
        data.runningTime += FFContext.timer.timeElapsed
        if (data.runningTime > data.duration) {
            if (looping) {
                if (data.inverseOnLoop) {
                    val tmp = data.startValue
                    data.startValue = data.endValue
                    data.endValue = tmp
                }
                reset()
            } else {
                AnimationSystem.animations.deactivate(index)
            }
            return
        }

        val t: Float = data.runningTime.toFloat() / data.duration
        var value = data.changeInValue * data.easing.calc(t)
        if (data.inverse) {
            value *= -1
        }

        propertyAccessor?.set(data.startValue + value)
    }

    companion object : EntityPropertyAnimation.Builder<EasedProperty>(), ISubType<EasedProperty, Animation> {
        override fun subType(): Class<EasedProperty> = EasedProperty::class.java
        override val typeKey: IIndexedTypeKey = Animation.typeKey
        override fun createEmpty(): EasedProperty =
            EasedProperty()
    }
}