
package com.inari.util.graphics

/** A simple color value with read/green/blue and alpha value in float precision.
 *
 * Use this if a simple string value configurable color value within float precision is needed.
 *
 * @author andreashefti
 */
class RGBColor {

    /** The red ratio value of the color  */
    var r: Float = 0f
        set(value) {field = adjustValue(value)}
    /** The green ratio value of the color  */
    var g: Float = 0f
        set(value) {field = adjustValue(value)}
    /** The blue ratio value of the color  */
    var b: Float = 0f
        set(value) {field = adjustValue(value)}
    /** The alpha ratio value of the color  */
    var a: Float = -1f
        set(value) {field = adjustValue(value)}

    constructor() {
        a = 1f
    }
    constructor(r: Float, g: Float, b: Float) {
        this.r = r
        this.g = g
        this.b = b
    }
    constructor(r: Float, g: Float, b: Float, a: Float) {
        this.r = r
        this.g = g
        this.b = b
        this.a = a
    }

    /** Copy constructor  */
    constructor(source: RGBColor) : this(source.r, source.g, source.b, source.a)

    operator fun invoke(r: Float, g: Float, b: Float) {
        this.r = r
        this.g = g
        this.b = b
    }

    operator fun invoke(r: Float, g: Float, b: Float, a: Float) {
        this.r = r
        this.g = g
        this.b = b
        this.a = a
    }

    operator fun invoke(color: RGBColor) {
        this.r = color.r
        this.g = color.g
        this.b = color.b
        this.a = color.a
    }

    val rgbA8888: Int
        get() = (r * 255).toInt() shl 24 or
            ((g * 255).toInt() shl 16) or
            ((b * 255).toInt() shl 8) or
            (a * 255).toInt()

    val rgB8888: Int
        get() = (r * 255).toInt() shl 24 or
            ((g * 255).toInt() shl 16) or
            ((b * 255).toInt() shl 8) or
            255



    /** Indicates if this RGBColor has an alpha ratio value (a >= 0)*/
    val hasAlpha: Boolean get() = a >= 0

    private fun adjustValue(value: Float): Float =
        when {
            value > 1.0f -> 1.0f
            value < 0.0f -> 0.0f
            else -> value
        }

    override fun toString(): String =
        "[r=$r,g=$g,b=$b,a=$a]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RGBColor

        if (r != other.r) return false
        if (g != other.g) return false
        if (b != other.b) return false
        if (a != other.a) return false
        if (hasAlpha != other.hasAlpha) return false

        return true
    }

    override fun hashCode(): Int {
        var result = r.hashCode()
        result = 31 * result + g.hashCode()
        result = 31 * result + b.hashCode()
        result = 31 * result + a.hashCode()
        result = 31 * result + hasAlpha.hashCode()
        return result
    }

    companion object {

        val black: RGBColor get() = RGBColor(0f, 0f, 0f, 1f)
        val white: RGBColor get() = RGBColor(1f, 1f, 1f, 1f)
        val red:  RGBColor get() = RGBColor(1f, 0f, 0f, 1f)
        val green:  RGBColor get() = RGBColor(0f, 1f, 0f, 1f)
        val blu:  RGBColor get() = RGBColor(0f, 0f, 1f, 1f)

        /** Create new RGBColor with specified r/g/b ratio values and no alpha (-1.0f)
         * @param r The red ratio value of the color: 0 - 255
         * @param g The green ratio value of the color: 0 - 255
         * @param b The blue ratio value of the color: 0 - 255
         */
        fun of(r: Int, g: Int, b: Int): RGBColor {
            return RGBColor(r / 255f, g / 255f, b / 255f)
        }

        /** Create new RGBColor with specified r/g/b/a ratio values
         * @param r The red ratio value of the color: 0 - 255
         * @param g The green ratio value of the color: 0 - 255
         * @param b The blue ratio value of the color: 0 - 255
         * @param a The alpha ratio value of the color: 0 - 255
         */
        fun of(r: Int, g: Int, b: Int, a: Int): RGBColor {
            return RGBColor(r / 255f, g / 255f, b / 255f, a / 255f)
        }
    }

}
