package com.inari.util.geom

/** Calculate easing for current time (t) for specified start value (b) and change in value (c) for a given duration of the Easing (d)
 * @param t current time
 * @param b start value
 * @param c change in value
 * @param d duration
 * @return the value for current time
 */
typealias EasingFunc = (t: Float, b: Float, c: Float, d: Float) -> Float

/** Interface to calculate a specified subType of Easing  */
interface Easing {

    /** Defines a Easing repository with different types of Easing implementations  */
    enum class Type constructor(val easing: EasingFunc) {
        LINEAR({ t,b,c,d ->
            c * t / d + b
        }),
        QUAD_IN({ t,b,c,d ->
            val tt = t / d
            c * tt * tt + b
        }),
        QUAD_OUT({ t,b,c,d ->
            val tt = t / d
             -c * tt * (tt - 2) + b
        }),
        QUAD_IN_OUT({ t,b,c,d ->
            var tt = t / (d / 2)
            if (tt < 1) c / 2 * tt * tt + b
            else {
                tt--
                -c / 2 * (tt * (tt - 2) - 1) + b
            }
        }),
        CUBIC_IN({ t,b,c,d ->
            val tt = t / d
            c * tt * tt * tt + b
        }),
        CUBIC_OUT({ t, b, c, d ->
            var tt = t / d
            tt--
            c * (tt * tt * tt + 1) + b
        }),
        CUBIC_IN_OUT({ t,b,c,d ->
            var tt = t / (d / 2)
            if (tt < 1) c / 2 * tt * tt * tt + b
            else {
                tt -= 2
                c / 2 * (tt * tt * tt + 2) + b
            }
        }),
        QRT_IN({ t,b,c,d ->
            val tt = t / d
            c * tt * tt * tt * tt + b
        }),
        QRT_OUT({ t,b,c,d ->
            var tt = t / d
            tt--
            -c * (tt * tt * tt * tt - 1f) + b
        }),
        QRT_IN_OUT({ t,b,c,d ->
            var tt = t / (d / 2)
            if (tt < 1) c / 2 * tt * tt * tt * tt + b
            else {
                tt -= 2
                -c / 2 * (tt * tt * tt * tt - 2) + b
            }
        }),
        QNT_IN({ t,b,c,d ->
            val tt = t / d
            c * tt * tt * tt * tt * tt + b
        }),
        QNT_OUT({ t,b,c,d ->
            var tt = t / d
            tt--
            c * (tt * tt * tt * tt * tt + 1) + b
        }),
        QNT_IN_OUT({ t,b,c,d ->
            var tt = t / (d / 2)
            if (tt < 1) c / 2 * tt * tt * tt * tt * tt + b
            else {
                tt -= 2
                c / 2 * (tt * tt * tt * tt * tt + 2) + b
            }
        }),
        EXPO_IN({ t,b,c,d ->
            c * GeomUtils.powf(2f, 10 * (t / d - 1)) + b
        }),
        EXPO_OUT({ t,b,c,d ->
            c * (-GeomUtils.powf(2f, -10 * t / d) + 1) + b
        }),
        EXPO_IN_OUT({ t,b,c,d ->
            var tt = t / (d / 2)
            if (tt < 1) c / 2 * GeomUtils.powf(2f, 10 * (tt - 1)) + b
            else {
                tt--
                c / 2 * (-GeomUtils.powf(2f, -10 * tt) + 2) + b
            }
        }),
        SIN_IN({ t,b,c,d ->
            -c * GeomUtils.cosf(t / d * GeomUtils.PI_2_F) + c + b
        }),
        SIN_OUT({ t,b,c,d ->
            c * GeomUtils.sinf(t / d * GeomUtils.PI_2_F) + b
        }),
        SIN_IN_OUT({ t,b,c,d ->
            -c / 2 * (GeomUtils.cosf(GeomUtils.PI_F * t / d) - 1) + b
        }),
        CIRC_IN({ t,b,c,d ->
            val tt = t / d
            -c * (GeomUtils.sqrtf(1 - tt * tt) - 1) + b
        }),
        CIRC_OUT({ t,b,c,d ->
            var tt = t / d
            tt--
            c * GeomUtils.sqrtf(1 - tt * tt) + b
        }),
        CIRC_IN_OUT({ t,b,c,d ->
            var tt = t / (d / 2)
            if (tt < 1f) -c / 2 * (GeomUtils.sqrtf(1 - tt * tt) - 1) + b
            else {
                tt -= 2
                c / 2 * (GeomUtils.sqrtf(1 - tt * tt) + 1) + b
            }
        });

        operator fun invoke(t: Float, b: Float, c: Float, d: Float): Float =
            this.easing(t, b, c, d)

        operator fun invoke(t: Int, b: Float, c: Float, d: Int): Float =
            this.easing(t.toFloat(), b, c, d.toFloat())

        operator fun invoke(t: Int, b: Float, c: Float, d: Long): Float =
            this.easing(t.toFloat(), b, c, d.toFloat())
    }

}
