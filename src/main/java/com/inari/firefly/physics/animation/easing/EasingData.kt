package com.inari.firefly.physics.animation.easing

import com.inari.firefly.FFContext

internal class EasingData {

    @JvmField var easing: Easing.EasingFunction = Easing.linear
    @JvmField var startValue = 0f
    @JvmField var endValue = 0f
    @JvmField var duration: Long = 0
    @JvmField var inverseOnLoop = false

    @JvmField var inverse = false
    @JvmField var changeInValue = 0f
    @JvmField var runningTime: Long = 0

    @JvmField var value = startValue

    fun reset() {
        runningTime = 0
        value = startValue
        changeInValue  = endValue - startValue
        if (changeInValue < 0) {
            inverse = true
            changeInValue = Math.abs(changeInValue)
        } else {
            inverse = false
        }
    }

    fun update(looping: Boolean): Boolean {
        runningTime += FFContext.timer.timeElapsed
        if (runningTime > duration) {
            return if (looping) {
                if (inverseOnLoop) {
                    val tmp = startValue
                    startValue = endValue
                    endValue = tmp
                }
                reset()
                true
            } else {
                false
            }
        }

        val t: Float = runningTime.toFloat() / duration
        value = changeInValue * easing.calc(t)
        if (inverse) {
            value *= -1
        }

        return true
    }
}