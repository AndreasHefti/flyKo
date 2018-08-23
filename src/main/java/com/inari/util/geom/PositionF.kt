package com.inari.util.geom

import com.inari.util.StringUtils
import java.util.StringTokenizer




/** A simple position in a 2D Cartesian coordinate system with float precision.  */
data class PositionF constructor(
    @JvmField var x: Float = 0.0f,
    @JvmField var y: Float = 0.0f
){

    /** Use this to create a new Position with specified initialization
     *
     * @param x The x axis value of the position
     * @param y The y axis value of the position
     */
    constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())
    /** Use this as a copy constructor  */
    constructor(loc: PositionF) : this(loc.x, loc.y)
    constructor(stringValue: String) : this() {
        fromConfigString(stringValue)
    }

    operator fun invoke(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    operator fun invoke(x: Int, y: Int) {
        this.x = x.toFloat()
        this.y = y.toFloat()
    }

    /** Use this to set the x/y axis values from specified Position p
     * @param p the Position to get/take the attributes from
     */
    operator fun invoke(p: Position) {
        x = p.x.toFloat()
        y = p.y.toFloat()
    }

    /** Use this to set the x/y axis values from specified PositionF p
     * @param p the PositionF to get/take the attributes from
     */
    operator fun invoke(p: PositionF) {
        x = p.x
        y = p.y
    }

    operator fun plus(pos: PositionF): PositionF {
        this.x += pos.x
        this.y += pos.y
        return this
    }

    operator fun plus(offset: Vector2f): PositionF {
        this.x += offset.dx
        this.y += offset.dy
        return this
    }

    operator fun plus(pos: Position): PositionF {
        this.x += pos.x.toFloat()
        this.y += pos.y.toFloat()
        return this
    }

    operator fun minus(pos: PositionF): PositionF {
        this.x -= pos.x
        this.y -= pos.y
        return this
    }

    operator fun minus(pos: Position): PositionF {
        this.x -= pos.x.toFloat()
        this.y -= pos.y.toFloat()
        return this
    }

    operator fun minus(offset: Vector2f): PositionF {
        this.x -= offset.dx
        this.y -= offset.dy
        return this
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
            x = 0f
            y = 0f
            return
        }

        if (!stringValue.contains(StringUtils.VALUE_SEPARATOR_STRING)) {
            throw IllegalArgumentException("The stringValue as invalid format: $stringValue")
        }

        val st = StringTokenizer(stringValue, StringUtils.VALUE_SEPARATOR_STRING)
        x = st.nextToken()?.toFloat() ?: 0f
        y = st.nextToken()?.toFloat() ?: 0f
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
        other as PositionF
        if (x != other.x || y != other.y) return false
        return true
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("[x=")
        builder.append(x)
        builder.append(",y=")
        builder.append(y)
        builder.append("]")
        return builder.toString()
    }
}
