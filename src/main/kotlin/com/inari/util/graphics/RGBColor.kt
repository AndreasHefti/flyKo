
package com.inari.util.graphics

/** A simple color value with read/green/blue and alpha value in float precision.
 *
 * Use this if a simple string value configurable color value within float precision is needed.
 *
 * @author andreashefti
 */
class RGBColor {

    /** The red ratio value of the color  */
    var r: Float
        set(value) {field = adjustValue(value)}
    /** The green ratio value of the color  */
    var g: Float
        set(value) {field = adjustValue(value)}
    /** The blue ratio value of the color  */
    var b: Float
        set(value) {field = adjustValue(value)}
    /** The alpha ratio value of the color  */
    var a: Float
        set(value) {field = adjustValue(value)}

    private val immutable: Boolean

    constructor(r: Float = 0f, g: Float = 0f, b: Float = 0f, a: Float = 1f, immutable: Boolean = false) {
        this.r = r
        this.g = g
        this.b = b
        this.a = a
        this.immutable = immutable
    }

    /** Copy constructor  */
    constructor(source: RGBColor) : this(source.r, source.g, source.b, source.a)

    operator fun invoke(r: Float = 0f, g: Float = 0f, b: Float = 0f, a: Float = 1f) {
        if (this.immutable)
            throw IllegalAccessException("Immutable")

        this.r = r
        this.g = g
        this.b = b
        this.a = a
    }

    operator fun invoke(color: RGBColor) {
        if (this.immutable)
            throw IllegalAccessException("Immutable")

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
    val hasAlpha: Boolean get() = a < 1f

    private fun adjustValue(value: Float): Float {
        if (this.immutable)
            throw IllegalAccessException("Immutable")

        return when {
            value > 1.0f -> 1.0f
            value < 0.0f -> 0.0f
            else -> value
        }
    }


    override fun toString(): String =
        "[r=$r,g=$g,b=$b,a=$a]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this::class != other::class) return false

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

        @JvmField val BLACK: RGBColor = RGBColor(0f, 0f, 0f, 1f, true)
        @JvmField val WHITE: RGBColor = RGBColor(1f, 1f, 1f, 1f, true)
        @JvmField val RED:  RGBColor = RGBColor(1f, 0f, 0f, 1f, true)
        @JvmField val GREEN:  RGBColor = RGBColor(0f, 1f, 0f, 1f, true)
        @JvmField val BLU:  RGBColor = RGBColor(0f, 0f, 1f, 1f, true)

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
