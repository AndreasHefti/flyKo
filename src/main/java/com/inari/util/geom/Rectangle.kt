package com.inari.util.geom

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

    override fun toString(): String =
        "[x=${pos.x},y=${pos.y},width=$width,height=$height]"
}
