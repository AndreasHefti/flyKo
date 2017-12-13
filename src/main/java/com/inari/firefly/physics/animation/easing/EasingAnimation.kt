package com.inari.firefly.physics.animation.easing

import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.commons.lang.list.DynArray
import com.inari.firefly.FFContext
import com.inari.firefly.component.Singleton
import com.inari.firefly.physics.animation.AnimatedProperty
import com.inari.firefly.physics.animation.Animation
import com.inari.firefly.system.component.SingletonComponent

@Singleton
class EasingAnimation private constructor(): Animation() {

    @JvmField internal val propertyAnimations =
        DynArray.create(EasingAnimated::class.java)

    override fun register(animated: AnimatedProperty) {
        if (animated is EasingAnimated)
            propertyAnimations.add(animated)
        else wrongAnimatedType("EasingAnimated", animated::class.java.name)
    }

    override fun dispose(animated: AnimatedProperty) {
        if (animated is EasingAnimated)
            propertyAnimations.remove(animated)
         else wrongAnimatedType("EasingAnimated", animated::class.java.name)
    }

    override fun invoke() {
        var i = 0
        while (i < propertyAnimations.capacity()) {
            val propertyAnimation = propertyAnimations[i++] ?: continue
            if (propertyAnimation.active)
                update(propertyAnimation)
        }
    }

    private fun update(animated: EasingAnimated) {
        animated.runningTime += FFContext.timer.timeElapsed
        if (animated.runningTime > animated.duration) {
            if (animated.looping) {
                if (animated.inverseOnLoop) {
                    val tmp = animated.startValue
                    animated.startValue = animated.endValue
                    animated.endValue = tmp
                }
                animated.reset()
            } else {
                animated.deactivate()
            }
            return
        }

        val value =
            if (animated.endValue - animated.startValue < 0 )
                animated.endValue - animated.easingType.easing.calc(
                    animated.runningTime.toFloat(),
                    animated.endValue,
                    animated.startValue,
                    animated.duration.toFloat()) - animated.startValue
            else
                animated.easingType.easing.calc(
                    animated.runningTime.toFloat(),
                    animated.startValue,
                    animated.endValue,
                    animated.duration.toFloat() )

        animated.propertyAccessor?.set(value)
    }

    companion object : SingletonComponent<EasingAnimation, Animation>() {
        override val typeKey: IndexedTypeKey = Animation.typeKey
        override fun subType() = EasingAnimation::class.java
        override fun create(): EasingAnimation =
            EasingAnimation()
    }
}