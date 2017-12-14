package com.inari.firefly.physics.animation.easing

import com.inari.commons.lang.indexed.IIndexedTypeKey
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
        data.reset()
        propertyAccessor?.set(data.startValue)
    }

    override fun update() {
        if (data.update(looping))
            propertyAccessor?.set(data.startValue + value)
        else
            AnimationSystem.animations.deactivate(index)
    }

    companion object : EntityPropertyAnimation.Builder<EasedProperty>(), ISubType<EasedProperty, Animation> {
        override fun subType(): Class<EasedProperty> = EasedProperty::class.java
        override val typeKey: IIndexedTypeKey = Animation.typeKey
        override fun createEmpty(): EasedProperty =
            EasedProperty()
    }
}