package com.inari.util.geom



import com.inari.util.StringUtils
import java.util.BitSet

class BitMask constructor(
    x: Int = 0,
    y: Int = 0,
    width: Int,
    height: Int
){

    private val region = Rectangle()
    private val bits: BitSet

    private val tmpRegion = Rectangle()
    private val intersection = Rectangle()

    private val tmpBits: BitSet

    val isEmpty: Boolean
        get() = bits.isEmpty

    init {
        region(x, y, width, height)
        bits = BitSet(region.width + region.height)
        tmpBits = BitSet(region.width + region.height)
    }

    constructor(region: Rectangle) :
        this(region.pos.x, region.pos.y, region.width, region.height)

    fun region(): Rectangle = region

    fun fill(): BitMask {
        clearMask()
        bits.set(0, region.width * region.height, true)
        return this
    }

    fun reset(region: Rectangle) =
        reset(region.pos.x, region.pos.y, region.width, region.height)

    fun reset(x: Int, y: Int, width: Int, height: Int) {
        region(x, y, width, height)
        bits.clear()
        tmpBits.clear()
    }

    fun clearMask() {
        bits.clear()
        tmpBits.clear()
    }

    fun setBit(index: Int) =
        bits.set(index)

    operator fun set(index: Int, flag: Boolean) =
        bits.set(index, flag)

    fun setBitRelativeToOrigin(x: Int, y: Int, flag: Boolean) {
        this[x - region.pos.x, y - region.pos.y] = flag
    }

    operator fun set(x: Int, y: Int, flag: Boolean) {
        if (x < 0 || x >= region.width || y < 0 || y >= region.height) {
            return
        }

        bits.set(y * region.width + x, flag)
    }

    fun getBit(x: Int, y: Int): Boolean =
        bits.get(y * region.width + x)

    operator fun invoke(x: Int, y: Int): Boolean =
        bits.get(y * region.width + x)

    operator fun plus(pos: Position)  =
        region.pos + pos

    operator fun set(reg: Rectangle, flag: Boolean) {
        setIntersectionRegion(
            reg.pos.x + region.pos.x,
            reg.pos.y + region.pos.y,
            reg.width, reg.height, flag
        )
    }

    fun setRegionRelativeToOrigin(reg: Rectangle, flag: Boolean) {
        setIntersectionRegion(reg.pos.x, reg.pos.y, reg.width, reg.height, flag)
    }

    private fun setIntersectionRegion(xOffset: Int, yOffset: Int, width: Int, height: Int, flag: Boolean) {
        tmpRegion(xOffset, yOffset, width, height)
        GeomUtils.intersection(region, tmpRegion, intersection)
        if (intersection.area() <= 0)
            return

        val x1 = intersection.pos.x - region.pos.x
        val y1 = intersection.pos.y - region.pos.y
        val width1 = x1 + intersection.width
        val height1 = y1 + intersection.height

        var y = 0
        while(y < height1) {
            var x = 0
            while(x < width1) {
                bits.set(y * region.width + x, flag)
                x++
            }
            y++
        }
    }

    fun and(other: BitMask): BitMask {
        setTmpBits(other, 0, 0)
        bits.and(tmpBits)
        return this
    }

    fun and(other: BitMask, xOffset: Int, yOffset: Int) {
        setTmpBits(other, xOffset, yOffset)
        bits.and(tmpBits)
    }

    fun or(other: BitMask) {
        setTmpBits(other, 0, 0)
        bits.or(tmpBits)
    }

    fun or(other: BitMask, xOffset: Int, yOffset: Int) {
        setTmpBits(other, xOffset, yOffset)
        bits.or(tmpBits)
    }

    private fun setTmpBits(other: BitMask, xOffset: Int, yOffset: Int) {
        tmpRegion(
            other.region.pos.x + xOffset,
            other.region.pos.y + yOffset,
            other.region.width,
            other.region.height
        )
        GeomUtils.intersection(region, tmpRegion, intersection)
        if (intersection.area() <= 0) {
            return
        }

        tmpBits.clear()

        // adjust intersection to origin
        val x1 = intersection.pos.x - region.pos.x
        val y1 = intersection.pos.y - region.pos.y
        val x2 = if (intersection.pos.x == 0) other.region.width - intersection.width
            else intersection.pos.x - tmpRegion.pos.x
        val y2 = if (intersection.pos.y == 0) other.region.height - intersection.height
            else intersection.pos.y - tmpRegion.pos.y

        var y = 0
        while(y < intersection.height) {
            var x = 0
            while(x < intersection.width) {
                tmpBits.set(
                    (y + y1) * region.width + (x + x1),
                    other.bits.get((y + y2) * other.region.width + (x + x2))
                )
                x++
            }
            y++
        }
    }

    fun hasIntersection(region: Rectangle): Boolean {
        GeomUtils.intersection(this.region, region, intersection)
        if (intersection.area() <= 0) {
            return false
        }

        val x1 = intersection.pos.x - this.region.pos.x
        val y1 = intersection.pos.y - this.region.pos.y
        val width1 = x1 + intersection.width
        val height1 = y1 + intersection.height

        var y = 0
        while (y < height1) {
            var x = 0
            while (x < width1) {
                if (bits.get(y * this.region.width + x)) {
                    return true
                }
                x++
            }
            y++
        }

        return false
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("BitMask [region=")
        builder.append(region)
        builder.append(", bits=\n")
        builder.append(StringUtils.bitSetToString(bits, region.width, region.height))
        builder.append("]")
        return builder.toString()
    }

    companion object {

        fun createIntersectionMask(region: Rectangle, bitmask: BitMask, result: BitMask, xoffset: Int, yoffset: Int, adjustResult: Boolean): Boolean {
            bitmask.region.pos.x += xoffset
            bitmask.region.pos.y += yoffset

            val intersection = createIntersectionMask(bitmask, region, result)

            bitmask.region.pos.x -= xoffset
            bitmask.region.pos.y -= yoffset

            if (adjustResult) {
                result.region.pos.x -= region.pos.x
                result.region.pos.y -= region.pos.y
            }

            return intersection
        }

        fun createIntersectionMask(bitmask: BitMask, region: Rectangle, result: BitMask, xoffset: Int, yoffset: Int, adjustResult: Boolean): Boolean {
            result.tmpRegion.pos.x = region.pos.x + xoffset
            result.tmpRegion.pos.y = region.pos.y + yoffset
            result.tmpRegion.width = region.width
            result.tmpRegion.height = region.height

            val intersection = createIntersectionMask(bitmask, result.tmpRegion, result)

            if (adjustResult) {
                result.region.pos.x -= bitmask.region.pos.x
                result.region.pos.y -= bitmask.region.pos.y
            }

            return intersection
        }

        fun createIntersectionMask(bitmask: BitMask, region: Rectangle, result: BitMask, adjustResult: Boolean): Boolean {
            val intersection = createIntersectionMask(bitmask, region, result)

            if (adjustResult) {
                result.region.pos.x -= bitmask.region.pos.x
                result.region.pos.y -= bitmask.region.pos.y
            }

            return intersection
        }

        fun createIntersectionMask(region: Rectangle, bitmask: BitMask, result: BitMask, adjustResult: Boolean): Boolean {
            val intersection = createIntersectionMask(bitmask, region, result)

            if (adjustResult) {
                result.region.pos.x -= region.pos.x
                result.region.pos.y -= region.pos.y
            }

            return intersection
        }

        fun createIntersectionMask(bitmask: BitMask, region: Rectangle, result: BitMask): Boolean {
            result.clearMask()

            GeomUtils.intersection(bitmask.region, region, result.region)
            if (result.region.area() <= 0) {
                return false
            }

            val x1 = result.region.pos.x - bitmask.region.pos.x
            val y1 = result.region.pos.y - bitmask.region.pos.y

            var y = 0
            while (y < result.region.height) {
                var x = 0
                while (x < result.region.width) {
                    result.bits.set(
                        y * result.region.width + x,
                        bitmask.bits.get((y + y1) * bitmask.region.width + (x + x1))
                    )
                    x++
                }
                y++
            }

            return !result.bits.isEmpty
        }

        fun createIntersectionMask(bitmask1: BitMask, bitmask2: BitMask, result: BitMask, xoffset: Int, yoffset: Int, adjustResult: Boolean): Boolean {
            bitmask2.region.pos.x += xoffset
            bitmask2.region.pos.y += yoffset

            val intersection = createIntersectionMask(bitmask1, bitmask2, result)

            bitmask2.region.pos.x -= xoffset
            bitmask2.region.pos.y -= yoffset

            if (adjustResult) {
                result.region.pos.x -= bitmask1.region.pos.x
                result.region.pos.y -= bitmask1.region.pos.y
            }

            return intersection
        }

        fun createIntersectionMask(bitmask1: BitMask, bitmask2: BitMask, result: BitMask, adjustResult: Boolean): Boolean {
            val intersection = createIntersectionMask(bitmask1, bitmask2, result)

            if (adjustResult) {
                result.region.pos.x -= bitmask1.region.pos.x
                result.region.pos.y -= bitmask1.region.pos.y
            }

            return intersection
        }

        fun createIntersectionMask(bitmask1: BitMask, bitmask2: BitMask, result: BitMask, xoffset: Int, yoffset: Int): Boolean {
            bitmask2.region.pos.x += xoffset
            bitmask2.region.pos.y += yoffset

            val intersection = createIntersectionMask(bitmask1, bitmask2, result)

            bitmask2.region.pos.x -= xoffset
            bitmask2.region.pos.y -= yoffset

            return intersection
        }

        fun createIntersectionMask(bitmask1: BitMask, bitmask2: BitMask, result: BitMask): Boolean {
            result.clearMask()


            GeomUtils.intersection(bitmask1.region, bitmask2.region, result.region)
            if (result.region.area() <= 0) {
                return false
            }

            val x1 = result.region.pos.x - bitmask1.region.pos.x
            val y1 = result.region.pos.y - bitmask1.region.pos.y
            val x2 = result.region.pos.x - bitmask2.region.pos.x
            val y2 = result.region.pos.y - bitmask2.region.pos.y

            var y = 0
            while (y < result.region.height) {
                var x = 0
                while (x < result.region.width) {
                    result.bits.set(
                        y * result.region.width + x,
                        bitmask1.bits.get(
                            (y + y1) * bitmask1.region.width + (x + x1)) &&
                            bitmask2.bits.get((y + y2) * bitmask2.region.width + (x + x2))
                    )
                    x++
                }
                y++
            }

            return !result.bits.isEmpty
        }
    }
}
