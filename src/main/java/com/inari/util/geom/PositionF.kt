package com.inari.util.geom

import sun.text.normalizer.UTF16.append




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

    /** Use this to set the x/y axis values from specified Position p
     * @param p the Position to get/take the attributes from
     */
    fun setFrom(p: Position) {
        x = p.x.toFloat()
        y = p.y.toFloat()
    }

    /** Use this to set the x/y axis values from specified PositionF p
     * @param p the PositionF to get/take the attributes from
     */
    fun setFrom(p: PositionF) {
        x = p.x
        y = p.y
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
