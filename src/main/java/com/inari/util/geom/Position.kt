package com.inari.util.geom

import sun.text.normalizer.UTF16.append




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

    /** Use this to set the x/y axis values from specified Position p
     * @param p the Position to get/take the attributes from
     */
    fun setFrom(p: Position) {
        x = p.x
        y = p.y
    }

    /** Use this to set the x/y axis values from specified Position p
     * NOTE: uses Math.floor to get the convert float to integer
     * @param p the Position to get/take the attributes from
     */
    fun setFrom(p: PositionF) {
        x = Math.floor(p.x.toDouble()).toInt()
        y = Math.floor(p.y.toDouble()).toInt()
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
