package com.inari.firefly.physics.animation.timeline

import com.inari.firefly.FFContext

internal class IntTimelineData {

    @JvmField var timeline: Array<Frame.IntFrame> = emptyArray()
    @JvmField var startValue = 0
    @JvmField var endValue = 0
    @JvmField var inverseOnLoop = false

    @JvmField var currentFrameTime: Long = 0
    @JvmField var currentIndex = startValue

    fun reset() {
        currentFrameTime = 0
        currentIndex = startValue
    }

    fun update(looping: Boolean): Boolean {
        val frame = timeline[currentIndex]

        currentFrameTime += FFContext.timer.timeElapsed

        if (currentFrameTime > frame.timeInterval) {
            currentIndex++
            if (currentIndex > endValue) {
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
        }

        return true
    }

}