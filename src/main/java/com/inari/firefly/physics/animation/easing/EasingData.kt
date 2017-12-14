package com.inari.firefly.physics.animation.easing

internal class EasingData {

    @JvmField var easing: Easing.EasingFunction = Easing.linear
    @JvmField var startValue = 0f
    @JvmField var endValue = 0f
    @JvmField var duration: Long = 0
    @JvmField var inverseOnLoop = false

    @JvmField var inverse = false
    @JvmField var changeInValue = 0f
    @JvmField var runningTime: Long = 0

}