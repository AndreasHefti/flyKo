package com.inari.util.geom

import com.inari.util.StringUtils
import java.util.StringTokenizer




/** A simple position in a 2D cartesian coordinate system with integer precision.
 * @param x The x axis value of the position
 * @param y The y axis value of the position
 * */
data class Position constructor(
    @JvmField var x: Int = 0,
    @JvmField var y: Int = 0
) {

    /** Use this as a copy constructor  */
    constructor(loc: Position) : this(loc.x, loc.y)
    constructor(stringValue: String) : this() {
        fromConfigString(stringValue)
    }

    operator fun invoke(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    operator fun plus(pos: Position): Position {
        this.x += pos.x
        this.y += pos.y
        return this
    }

    operator fun minus(pos: Position): Position {
        this.x -= pos.x
        this.y -= pos.y
        return this
    }

    /** Use this to set the x/y axis values from specified Position p
     * @param p the Position to get/take the attributes from
     */
    operator fun invoke(p: Position) {
        x = p.x
        y = p.y
    }

    /** Use this to set the x/y axis values from specified Position p
     * NOTE: uses Math.floor to get the convert float to integer
     * @param p the Position to get/take the attributes from
     */
    operator fun invoke(p: PositionF) {
        x = Math.floor(p.x.toDouble()).toInt()
        y = Math.floor(p.y.toDouble()).toInt()
    }

    /** Use this to set  the x/y axis values from specified configuration String value with the
     * format: [x],[y].
     *
     * @param stringValue the configuration String value
     * @throws IllegalArgumentException If the String value as a invalid format
     * @throws NumberFormatException if the x/y values from the String value aren't numbers
     */
    fun fromConfigString(stringValue: String) {
        if (StringUtils.isBlank(stringValue)) {
            x = 0
            y = 0
            return
        }

        if (!stringValue.contains(StringUtils.VALUE_SEPARATOR_STRING)) {
            throw IllegalArgumentException("The stringValue as invalid format: $stringValue")
        }

        val st = StringTokenizer(stringValue, StringUtils.VALUE_SEPARATOR_STRING)
        x = st.nextToken()?.toInt() ?: 0
        y = st.nextToken()?.toInt() ?: 0
    }

    /** Use this to get a configuration String value that represents this Position
     * and can be used to reset the attributes of a Position by using fromConfigString
     * The format is: [x],[y].
     * @return A configuration String value that represents this Position
     */
    fun toConfigString(): String {
        return x.toString() + StringUtils.VALUE_SEPARATOR_STRING + y
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Position
        if (x != other.x || y != other.y) return false
        return true
    }

    override fun toString(): String = "[x=$x,y=$y]"
}
