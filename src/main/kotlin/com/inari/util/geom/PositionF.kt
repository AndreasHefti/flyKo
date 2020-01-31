package com.inari.util.geom


/** A simple position in a 2D Cartesian coordinate system with float precision.  */
data class PositionF constructor(
    @JvmField var x: Float = 0.0f,
    @JvmField var y: Float = 0.0f
){

    /** Use this to create a new Position with specified initialization
     *
     * @param x The x-axis value of the position
     * @param y The y-axis value of the position
     */
    constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())
    /** Use this as a copy constructor  */
    constructor(loc: PositionF) : this(loc.x, loc.y)

    operator fun invoke(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    operator fun invoke(x: Int, y: Int) {
        this.x = x.toFloat()
        this.y = y.toFloat()
    }

    /** Use this to set the x/y-axis values from specified Position p
     * @param p the Position to get/take the attributes from
     */
    operator fun invoke(p: Position) {
        x = p.x.toFloat()
        y = p.y.toFloat()
    }

    /** Use this to set the x/y-axis values from specified PositionF p
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PositionF
        if (x != other.x || y != other.y) return false
        return true
    }

    override fun toString(): String = "[x=$x,y=$y]"

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}
