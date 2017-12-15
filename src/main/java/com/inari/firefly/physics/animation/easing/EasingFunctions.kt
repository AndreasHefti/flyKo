package com.inari.firefly.physics.animation.easing


object EasingFunctions {

    val pi = Math.PI
    val halfPi = pi / 2.0
    val tau = 2.0 * Math.PI

    @JvmField val linear = object : EasingFunction {
        override fun calc(t: Float): Float = t
    }

    fun polyIn(exp: Double = 3.0) = object : EasingFunction {
        override fun calc(t: Float): Float = Math.pow(t.toDouble(), exp).toFloat()
    }

    fun polyOut(exp: Double = 3.0) = object : EasingFunction {
        override fun calc(t: Float): Float = 1f - Math.pow(1.0 - t.toDouble(), exp).toFloat()
    }

    fun polyInOut(exp: Double = 3.0) = object : EasingFunction {
        override fun calc(t: Float): Float {
            val tt = t * 2f
            return (if (tt <= 1f)
                Math.pow(tt.toDouble(), exp)
            else
                2f - Math.pow(2.0 - tt, exp)
             ).toFloat() / 2f
        }
    }

    @JvmField val sinIn = object : EasingFunction {
        override fun calc(t: Float): Float = 1f - Math.cos(t * halfPi).toFloat()
    }

    @JvmField val sinOut = object : EasingFunction {
        override fun calc(t: Float): Float = Math.sin(t * halfPi).toFloat()
    }

    @JvmField val sinInOut = object : EasingFunction {
        override fun calc(t: Float): Float = (1f - Math.cos(pi * t)).toFloat() / 2f
    }

    @JvmField val expIn = object : EasingFunction {
        override fun calc(t: Float): Float = Math.pow(2.0, 10.0 * t - 10.0).toFloat()
    }

    @JvmField val expOut = object : EasingFunction {
        override fun calc(t: Float): Float = 1f - Math.pow(2.0, -10.0 * t).toFloat()
    }

    @JvmField val expInOut = object : EasingFunction {
        override fun calc(t: Float): Float {
            val tt = t * 2f
            return if (tt <= 1.0f)
                Math.pow(2.0, 10.0 * tt - 10).toFloat() / 2f
            else
                (2f - Math.pow(2.0, 10.0 - 10.0 * tt)).toFloat() / 2f
        }
    }

    @JvmField val circleIn = object : EasingFunction {
        override fun calc(t: Float): Float = 1.0f - Math.sqrt(1.0 - t * t).toFloat()
    }

    @JvmField val circleOut = object : EasingFunction {
        override fun calc(t: Float): Float {
            val tt = t - 1f
            return Math.sqrt(1.0 - tt * tt).toFloat()
        }
    }

    @JvmField val circleInOut = object : EasingFunction {
        override fun calc(t: Float): Float {
            val tt = t * 2f
            return if (tt <= 1.0f) {
                (1f - Math.sqrt(1.0 - tt * tt)).toFloat() / 2f
            } else {
                val ttt = tt - 2
                (Math.sqrt(1.0 - ttt * ttt) + 1f).toFloat() / 2f
            }
        }
    }

    fun elasticIn(amplitude: Float = 1f, period: Float = 0.3f) = object : EasingFunction {
        val a = Math.max(1f, amplitude)
        val p = period / tau
        val s = Math.asin(1.0 / a) * p
        override fun calc(t: Float): Float {
            val tt = t - 1f
            return (a * Math.pow(2.0, 10.0 * tt) * Math.sin((s - tt) / p)).toFloat()
        }
    }

    fun elasticOut(amplitude: Float = 1f, period: Float = 0.3f) = object : EasingFunction {
        val a = Math.max(1f, amplitude)
        val p = period / tau
        val s = Math.asin(1.0 / a) * p
        override fun calc(t: Float): Float {
            val tt = t + 1f
            return  (1f - a * Math.pow(2.0, -10.0 * tt) * Math.sin((tt + s) / p)).toFloat()
        }
    }

    fun elasticInOut(amplitude: Float = 1f, period: Float = 0.3f) = object : EasingFunction {
        val a = Math.max(1f, amplitude)
        val p = period / tau
        val s = Math.asin(1.0 / a) * p
        override fun calc(t: Float): Float {
            val tt = t * 2f - 1f
            return if (tt < 0)
                (a * Math.pow(2.0, 10.0 * tt) * Math.sin((s - tt) / p)).toFloat()
            else
                ((2f - a * Math.pow(2.0, -10.0 * tt) * Math.sin((s + tt) / p)) / 2f).toFloat()
        }
    }

    fun backIn(backFactor: Float = 1.70158f) = object : EasingFunction {
        override fun calc(t: Float): Float {
            return t * t * ((backFactor + 1f) * t - backFactor)
        }

    }

    fun backOut(backFactor: Float = 1.70158f) = object : EasingFunction {
        override fun calc(t: Float): Float {
            val tt = t - 1f
            return tt * tt * ((backFactor + 1f) * tt + backFactor) + 1f
        }

    }

    fun backInOut(backFactor: Float = 1.70158f) = object : EasingFunction {
        override fun calc(t: Float): Float {
            val tt = t * 2f
            return if (tt < 1f) {
                t * ((backFactor + 1f) * t - backFactor) / 2f
            } else {
                val ttt = tt - 2f
                (ttt * ttt * ((backFactor + 1f) * ttt + backFactor) + 2f) / 2f
            }
        }
    }

    fun bonceIn(
        b1: Float = 4f / 11f,
        b2: Float = 6f / 11f,
        b3: Float = 8f / 11f,
        b4: Float = 3f / 4f,
        b5: Float = 9f / 11f,
        b6: Float = 10f / 11f,
        b7: Float = 15f / 16f,
        b8: Float = 21f / 22f,
        b9: Float = 63 / 64f
    ) = object : EasingFunction {
        val bounceOut = bonceOut(b1, b2, b3, b4, b5, b6, b7, b8, b9)
        override fun calc(t: Float): Float =
            1f - bounceOut.calc(1f - t)
    }


    fun bonceOut(
        b1: Float = 4f / 11f,
        b2: Float = 6f / 11f,
        b3: Float = 8f / 11f,
        b4: Float = 3f / 4f,
        b5: Float = 9f / 11f,
        b6: Float = 10f / 11f,
        b7: Float = 15f / 16f,
        b8: Float = 21f / 22f,
        b9: Float = 63 / 64f
    ) = object : EasingFunction {
        val b0 = 1f / b1 / b1
        override fun calc(t: Float): Float {
            return when {
                t < b1 -> b0 * t * t
                t < b3 -> {
                    val tt = t - b2
                    b0 * tt * tt + b4
                }
                t < b6 -> {
                    val tt = t - b5
                    b0 * tt * tt + b7
                }
                else -> {
                    val tt = t - b8
                    b0 * tt * tt + b9
                }
            }
        }
    }

    fun bonceInOut(
        b1: Float = 4f / 11f,
        b2: Float = 6f / 11f,
        b3: Float = 8f / 11f,
        b4: Float = 3f / 4f,
        b5: Float = 9f / 11f,
        b6: Float = 10f / 11f,
        b7: Float = 15f / 16f,
        b8: Float = 21f / 22f,
        b9: Float = 63 / 64f
    ) = object : EasingFunction {
        val bounceOut = bonceOut(b1, b2, b3, b4, b5, b6, b7, b8, b9)
        override fun calc(t: Float): Float {
            val tt = t * 2f
            return if (tt < 1)
                (1f - bounceOut.calc(1f - t)) / 2f
            else
                (bounceOut.calc(t - 1f) + 1f) / 2f
        }
    }


    interface EasingFunction {

        /** EasingFunctions calculation as function of t in the interval of 0 to 1
         * @param t current time in the interval 0 to 1
         * @return the easing value for current time
         */
        fun calc(t: Float): Float
    }
}