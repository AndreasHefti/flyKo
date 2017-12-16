package com.inari.firefly.physics.animation.timeline

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.property.IntPropertyAccessor
import com.inari.firefly.physics.animation.Animation
import com.inari.firefly.physics.animation.IntAnimation
import com.inari.firefly.physics.animation.entity.EntityPropertyAnimation
import com.inari.firefly.system.component.ISubType

class IntTimelineProperty private constructor() : EntityPropertyAnimation(), IntAnimation {

    @JvmField internal var propertyAccessor: IntPropertyAccessor? = null
    @JvmField internal val data = IntTimelineData()

    var ff_Timeline: Array<Frame.IntFrame>
        get() = data.timeline
        set(value) { data.timeline = value }
    var ff_StartValue: Int
        get() = data.startValue
        set(value) { data.startValue = value }
    var ff_EndValue: Int
        get() = data.endValue
        set(value) { data.endValue = value }
    var ff_InverseOnLoop: Boolean
        get() = data.inverseOnLoop
        set(value) { data.inverseOnLoop = value }

    override val value: Int
        get() = propertyAccessor?.get() ?: -1

    override fun init(entity: Entity) {
        propertyAccessor = propertyRef.accessor(entity) as IntPropertyAccessor
    }

    override fun update() {
        if (data.update(looping))
            propertyAccessor?.set(data.timeline[data.currentIndex].value)
    }

    override fun reset() {
        data.reset()
        propertyAccessor?.set(data.startValue)
    }

    companion object : EntityPropertyAnimation.Builder<IntTimelineProperty>(), ISubType<IntTimelineProperty, Animation> {
        override fun subType(): Class<IntTimelineProperty> = IntTimelineProperty::class.java
        override val typeKey: IIndexedTypeKey = Animation.typeKey
        override fun createEmpty(): IntTimelineProperty =
            IntTimelineProperty()
    }

}