package com.inari.firefly.physics.animation.timeline

import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.property.IIntPropertyAccessor
import com.inari.firefly.physics.animation.AnimatedProperty

class IntTimelineAnimated private constructor() : AnimatedProperty() {

    @JvmField internal var propertyAccessor: IIntPropertyAccessor? = null

    @JvmField internal var timeline: Array<Frame> = emptyArray()
    @JvmField internal var startValue = 0
    @JvmField internal var endValue = 0
    @JvmField internal var inverseOnLoop = false

    @JvmField internal var currentFrameTime: Long = 0
    @JvmField internal var currentIndex = startValue

    var ff_Timeline: Array<Frame>
        get() = timeline
        set(value) { timeline = value }
    var ff_StartValue: Int
        get() = startValue
        set(value) { startValue = value }
    var ff_EndValue: Int
        get() = endValue
        set(value) { endValue = value }
    var ff_InverseOnLoop: Boolean
        get() = inverseOnLoop
        set(value) { inverseOnLoop = value }

    override fun init(entity: Entity) {
        propertyAccessor = propertyRef.accessor(entity) as IIntPropertyAccessor
        if (animationRef < 0)
            animationRef = IntTimelineAnimation
                .activate()
                .index()
    }

    override fun reset() {
        currentFrameTime = 0
        currentIndex = startValue
        propertyAccessor?.set(startValue)
    }

    companion object : AnimatedProperty.Builder<IntTimelineAnimated>() {
        override fun createEmpty() = IntTimelineAnimated()
    }

    data class Frame(
        @JvmField var value: Int,
        @JvmField var time: Long
    )
}