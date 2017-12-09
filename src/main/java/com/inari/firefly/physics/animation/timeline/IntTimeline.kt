package com.inari.firefly.physics.animation.timeline

import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.commons.lang.list.DynArray
import com.inari.firefly.FFContext
import com.inari.firefly.component.ArrayPropertyAccessor
import com.inari.firefly.physics.animation.AnimatedInt
import com.inari.firefly.physics.animation.AnimatedProperty
import com.inari.firefly.physics.animation.Animation
import com.inari.firefly.system.component.SubType

class IntTimeline : Animation() {

    @JvmField internal val propertyAnimations:DynArray<AnimatedInt> =
        DynArray.create(AnimatedInt::class.java, 2, 5)

    @JvmField internal val timeline: DynArray<IntFrame> =
        DynArray.create(IntFrame::class.java, 10, 20)

    val ff_Timeline =
        ArrayPropertyAccessor(timeline, trim = true)

    override fun register(animated: AnimatedProperty) {
        if (animated !is AnimatedInt) {
            throw IllegalArgumentException("IntTimeline animation accept only AnimatedProperty of type AnimatedInt")
        }
        propertyAnimations.add(animated)
    }

    override fun dispose(animated: AnimatedProperty) {
        propertyAnimations.remove(animated as AnimatedInt)
    }

    override fun invoke() {
        var i = 0
        while (i < propertyAnimations.capacity()) {
            val propertyAnimation = propertyAnimations[i++] ?: continue
            if (propertyAnimation.active)
                update(propertyAnimation)
        }
    }

    private fun update(t: AnimatedInt) {
        val time: Long = FFContext.timer.time
        var current = t.v1
        val frame: IntFrame = timeline[current]
        val lastUpdate = t.lastUpdate
        val upperBorder = if (t.v2 > t.initValue) t.v2 else timeline.size()

        if (lastUpdate < 0) {
            t.lastUpdate = time
            t.propertyAccessor.set(frame.value)
        }

        if (time - lastUpdate > frame.time) {
            current++
            if (current >= upperBorder) {
                if (t.looping) {
                    t.reset()
                } else {
                    t.deactivate()
                    return
                }
            }

            t.lastUpdate = time
            t.propertyAccessor.set(timeline[t.v1].value)
        }
    }

    companion object : SubType<IntTimeline, Animation>() {
        override val typeKey: IndexedTypeKey = Animation.typeKey
        override fun subType() = IntTimeline::class.java
        override fun createEmpty() = IntTimeline()
    }
}