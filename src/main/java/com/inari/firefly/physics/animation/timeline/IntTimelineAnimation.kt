package com.inari.firefly.physics.animation.timeline

import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.commons.lang.list.DynArray
import com.inari.firefly.FFContext
import com.inari.firefly.component.Singleton
import com.inari.firefly.physics.animation.AnimatedProperty
import com.inari.firefly.physics.animation.Animation
import com.inari.firefly.system.component.SubType

@Singleton
class IntTimelineAnimation : Animation() {

    @JvmField internal val propertyAnimations =
        DynArray.create(IntTimelineAimated::class.java, 2, 5)

    override fun register(animated: AnimatedProperty) {
        if (animated is IntTimelineAimated)
            propertyAnimations.add(animated)
        else wrongAnimatedType(IntTimelineAimated::class.java.name, animated::class.java.name)
    }

    override fun dispose(animated: AnimatedProperty) {
        if (animated is IntTimelineAimated)
            propertyAnimations.remove(animated)
        else wrongAnimatedType(IntTimelineAimated::class.java.name, animated::class.java.name)
    }

    override fun invoke() {
        var i = 0
        while (i < propertyAnimations.capacity()) {
            val propertyAnimation = propertyAnimations[i++] ?: continue
            if (propertyAnimation.active)
                update(propertyAnimation)
        }
    }

    private fun update(t: IntTimelineAimated) {
        val frame: IntTimelineAimated.Frame = t.timeline[t.currentIndex]

        t.currentFrameTime += FFContext.timer.timeElapsed

        if (t.currentFrameTime > frame.time) {
            t.currentIndex++
            if (t.currentIndex > t.endValue) {
                if (t.looping) {
                    if (t.inverseOnLoop) {
                        val tmp = t.startValue
                        t.startValue = t.endValue
                        t.endValue = tmp
                    }
                    t.reset()
                } else {
                    t.deactivate()
                }
                return
            }

            t.propertyAccessor.set(t.timeline[t.currentIndex].value)
        }
    }

    companion object : SubType<IntTimelineAnimation, Animation>() {
        override val typeKey: IndexedTypeKey = Animation.typeKey
        override fun subType() = IntTimelineAnimation::class.java
        override fun createEmpty() = IntTimelineAnimation()
    }
}