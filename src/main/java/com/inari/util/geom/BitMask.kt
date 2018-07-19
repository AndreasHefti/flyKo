package com.inari.util.geom



import com.inari.util.StringUtils
import java.util.BitSet



class BitMask constructor(
    x: Int = 0,
    y: Int = 0,
    width: Int = 0,
    height: Int = 0
){

    private val region = Rectangle()
    private var bits: BitSet

    private val tmpRegion = Rectangle()
    private val intersection = Rectangle()

    private var tmpBits: BitSet

    constructor(region: Rectangle) : this(region.pos.x, region.pos.y, region.width, region.height)

    init {
        region.pos.x = x
        region.pos.y = y
        region.width = width
        region.height = height
        bits = BitSet(region.width + region.height)
        tmpBits = BitSet(region.width + region.height)
    }

    fun region(): Rectangle =
        region
    val isEmpty: Boolean get() =
        bits.isEmpty

    fun fill(): BitMask {
        clearMask()
        for (i in 0 until region.width * region.height) {
            bits.set(i)
        }
        return this
    }

    fun reset(region: Rectangle) {
        reset(region.pos.x, region.pos.y, region.width, region.height)
    }

    fun reset(x: Int, y: Int, width: Int, height: Int) {
        region.pos.x = x
        region.pos.y = y
        region.width = width
        region.height = height
        bits.clear()
        tmpBits.clear()
    }

    fun clearMask() {
        bits.clear()
        tmpBits.clear()
    }

    fun setBit(index: Int) {
        bits.set(index)
    }

    fun setBit(x: Int, y: Int, relativeToOrigin: Boolean) {
        if (relativeToOrigin) {
            setBit(x - region.pos.x, y - region.pos.y)
        } else {
            setBit(x, y)
        }
    }

    fun setBit(x: Int, y: Int) {
        if (x < 0 || x >= region.width || y < 0 || y >= region.height) {
            return
        }

        bits.set(y * region.width + x)
    }

    fun resetBit(index: Int) {
        bits.set(index, false)
    }

    fun resetBit(x: Int, y: Int, relativeToOrigin: Boolean) {
        if (relativeToOrigin) {
            resetBit(x - region.pos.x, y - region.pos.y)
        } else {
            resetBit(x, y)
        }
    }

    fun resetBit(x: Int, y: Int) {
        if (x < 0 || x >= region.width || y < 0 || y >= region.height) {
            return
        }

        bits.set(y * region.width + x, false)
    }

    fun getBit(x: Int, y: Int): Boolean {
        return bits.get(y * region.width + x)
    }

    fun moveRegion(x: Int, y: Int) {
        region.pos.x += x
        region.pos.y += y
    }

    fun setRegion(region: Rectangle, relativeToOrigin: Boolean) {
        setRegion(region.pos.x, region.pos.y, region.width, region.height, relativeToOrigin)
    }

    fun setRegion(x: Int, y: Int, width: Int, height: Int) {
        setRegion(x, y, width, height, true)
    }

    fun setRegion(x: Int, y: Int, width: Int, height: Int, relativeToOrigin: Boolean) {
        if (relativeToOrigin) {
            setIntersectionRegion(x, y, width, height, true)
        } else {
            setIntersectionRegion(x + region.pos.x, y + region.pos.y, width, height, true)
        }
    }

    fun resetRegion(region: Rectangle, relativeToOrigin: Boolean) {
        resetRegion(region.pos.x, region.pos.y, region.width, region.height, relativeToOrigin)
    }

    fun resetRegion(x: Int, y: Int, width: Int, height: Int) {
        resetRegion(x, y, width, height, true)
    }

    fun resetRegion(x: Int, y: Int, width: Int, height: Int, relativeToOrigin: Boolean) {
        if (relativeToOrigin) {
            setIntersectionRegion(x, y, width, height, false)
        } else {
            setIntersectionRegion(x + region.pos.x, y + region.pos.y, width, height, false)
        }
    }

    private fun setIntersectionRegion(xoffset: Int, yoffset: Int, width: Int, height: Int, set: Boolean) {
        tmpRegion.pos.x = xoffset
        tmpRegion.pos.y = yoffset
        tmpRegion.width = width
        tmpRegion.height = height
        GeomUtils.intersection(region, tmpRegion, intersection)
        if (intersection.area() <= 0) {
            return
        }

        val x1 = intersection.pos.x - region.pos.x
        val y1 = intersection.pos.y - region.pos.y
        val width1 = x1 + intersection.width
        val height1 = y1 + intersection.height

        for (y in y1 until height1) {
            for (x in x1 until width1) {
                bits.set(y * region.width + x, set)
            }
        }
    }

    fun and(other: BitMask) {
        setTmpBits(other, 0, 0)
        bits.and(tmpBits)
    }

    fun and(other: BitMask, xoffset: Int, yoffset: Int) {
        setTmpBits(other, xoffset, yoffset)
        bits.and(tmpBits)
    }

    fun or(other: BitMask) {
        setTmpBits(other, 0, 0)
        bits.or(tmpBits)
    }

    fun or(other: BitMask, xoffset: Int, yoffset: Int) {
        setTmpBits(other, xoffset, yoffset)
        bits.or(tmpBits)
    }

    private fun setTmpBits(other: BitMask, xoffset: Int, yoffset: Int) {
        tmpRegion.pos.x = other.region.pos.x + xoffset
        tmpRegion.pos.y = other.region.pos.y + yoffset
        tmpRegion.width = other.region.width
        tmpRegion.height = other.region.height
        GeomUtils.intersection(region, tmpRegion, intersection)
        if (intersection.area() <= 0) {
            return
        }

        tmpBits.clear()

        // adjust intersection to origin
        val x1 = intersection.pos.x - region.pos.x
        val y1 = intersection.pos.y - region.pos.y
        val x2 = if (intersection.pos.x == 0) other.region.width - intersection.width else intersection.pos.x - tmpRegion.pos.x
        val y2 = if (intersection.pos.y == 0) other.region.height - intersection.height else intersection.pos.y - tmpRegion.pos.y

        for (y in 0 until intersection.height) {
            for (x in 0 until intersection.width) {
                tmpBits.set((y + y1) * region.width + (x + x1), other.bits.get((y + y2) * other.region.width + (x + x2)))
            }
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

        for (y in y1 until height1) {
            for (x in x1 until width1) {
                if (bits.get(y * this.region.width + x)) {
                    return true
                }
            }
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

            for (y in 0 until result.region.height) {
                for (x in 0 until result.region.width) {
                    result.bits.set(
                        y * result.region.width + x,
                        bitmask.bits.get((y + y1) * bitmask.region.width + (x + x1))
                    )
                }
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

            for (y in 0 until result.region.height) {
                for (x in 0 until result.region.width) {
                    result.bits.set(
                        y * result.region.width + x,
                        bitmask1.bits.get((y + y1) * bitmask1.region.width + (x + x1)) && bitmask2.bits.get((y + y2) * bitmask2.region.width + (x + x2))
                    )
                }
            }

            return !result.bits.isEmpty
        }
    }
}