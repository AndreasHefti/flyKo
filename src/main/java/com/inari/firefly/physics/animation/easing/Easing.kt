package com.inari.firefly.physics.animation.easing

object Easing {

    interface EasingFunction {

        /** Easing calculation as function of t in the interval of 0 to 1
         * @param t current time in the interval 0 to 1
         * @return the easing value for current time
         */
        fun calc(t: Float): Float
    }

    @JvmField val linear = object : EasingFunction {
        override fun calc(t: Float): Float = t
    }

    fun polyIn(exp: Double) = object : EasingFunction {
        override fun calc(t: Float): Float = Math.pow(t.toDouble(), exp).toFloat()
    }

}