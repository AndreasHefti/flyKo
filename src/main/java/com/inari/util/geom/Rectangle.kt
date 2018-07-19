package com.inari.util.geom

import com.inari.util.StringUtils
import javax.swing.Spring.height
import java.util.StringTokenizer



/** A simple Rectangle with integer precision
 *  @param width The width of the Rectangle
 *  @param height The height of the Rectangle
 *  */
data class Rectangle constructor(
    @JvmField var pos: Position = Position(0, 0),
    @JvmField var width: Int = 0,
    @JvmField var height: Int = 0
){

    /** Use this to create a Rectangle on specified Position with specified width and height
     *
     * @param x the x-axis Position of the new Rectangle
     * @param y the y-axis Position of the new Rectangle
     * @param width the width of the new Rectangle
     * @param height the height of the new Rectangle
     */
    constructor(x: Int, y: Int, width: Int, height: Int) : this(Position(x, y), width, height)
    constructor(other: Rectangle) : this(other.pos, other.width, other.height)
    constructor(stringValue: String) : this() {
        fromConfigString(stringValue)
    }

    /** Use this to get the area value (width * height) form this Rectangle.
     * @return the area value (width * height) form this Rectangle.
     */
    fun area(): Int {
        return width * height
    }

    operator fun invoke(x: Int, y: Int) {
        pos(x, y)
    }

    operator fun invoke(x: Int, y: Int, w: Int, h: Int) {
        pos(x, y)
        width = w
        height = h
    }

    /** Use this to set the attributes of this Rectangle by another Rectangle.
     * @param other the other Rectangle to get/take the attributes from
     */
    operator fun invoke(other: Rectangle) {
        pos(other.pos)
        width = other.width
        height = other.height
    }

    operator fun set(x: Int, y: Int, other: Rectangle) {
        pos(x, y)
        width = other.width
        height = other.height
    }

    /** Use this to set  the Rectangle attributes from specified configuration String value with the
     * format: [x],[y],[width],[height].
     *
     * @param stringValue the configuration String value
     * @throws IllegalArgumentException If the String value as a invalid format
     * @throws NumberFormatException if the x/y/width/height values from the String value aren't numbers
     */
    fun fromConfigString(stringValue: String) {
        if (!StringUtils.isBlank(stringValue)) {
            val st = StringTokenizer(stringValue, StringUtils.VALUE_SEPARATOR_STRING)
            pos.x = Integer.valueOf(st.nextToken())!!.toInt()
            pos.y = Integer.valueOf(st.nextToken())!!.toInt()
            width = Integer.valueOf(st.nextToken())!!.toInt()
            height = Integer.valueOf(st.nextToken())!!.toInt()
        }
    }

    /** Use this to get a configuration String value that represents this Rectangle
     * and can be used to reset the attributes of a Rectangle by using fromConfigString
     * The format is: [x],[y],[width],[height].
     * @return A configuration String value that represents this Rectangle
     */
    fun toConfigString(): String {
        val sb = StringBuilder()
        sb.append(pos.x).append(StringUtils.VALUE_SEPARATOR).append(pos.y).append(StringUtils.VALUE_SEPARATOR)
        sb.append(width).append(StringUtils.VALUE_SEPARATOR).append(height)
        return sb.toString()
    }

    override fun toString(): String =
        "[x=${pos.x},y=${pos.y},width=$width,height=$height]"
}
