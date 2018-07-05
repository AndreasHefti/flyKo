package com.inari.util.geom


import com.inari.util.geom.Direction.*
import java.util.*


// TODO write tests
object GeomUtils {

    const val PI_F = Math.PI.toFloat()
    const val PI_2_F = PI_F / 2f

    const val NULL_POINT_STRING = "0,0"
    const val NULL_RECTANGLE_STRING = "0,0,0,0"

    const val TOP_SIDE = 1
    const val RIGHT_SIDE = 1 shl 2
    const val BOTTOM_SIDE = 1 shl 3
    const val LEFT_SIDE = 1 shl 4

    fun sqrtf(value: Float): Float =
        Math.sqrt(value.toDouble()).toFloat()

    fun powf(v1: Float, v2: Float): Float =
        Math.pow(v1.toDouble(), v2.toDouble()).toFloat()

    fun sinf(value: Float): Float =
        Math.sin(value.toDouble()).toFloat()

    fun cosf(value: Float): Float =
        Math.cos(value.toDouble()).toFloat()

    fun getDistance(p1: Position, p2: Position): Float {
        val dx = p2.x - p1.x
        val dy = p2.y - p1.y

        return Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }

    fun intersect(r1: Rectangle, r2: Rectangle): Boolean {
        val r1Right = r1.pos.x + r1.width
        val r1Bottom = r1.pos.y + r1.height
        val r2Right = r2.pos.x + r2.width
        val r2Bottom = r2.pos.y + r2.height

        return !(r2.pos.x >= r1Right ||
            r2Right <= r1.pos.x ||
            r2.pos.y >= r1Bottom ||
            r2Bottom <= r1.pos.y)
    }

    fun getIntersectionCode(r: Rectangle, r1: Rectangle): Int {
        val ax1 = r.pos.x
        val ay1 = r.pos.y
        val ax2 = r.pos.x + r.width - 1
        val ay2 = r.pos.y + r.height - 1

        val bx1 = r1.pos.x
        val by1 = r1.pos.y
        val bx2 = r1.pos.x + r1.width - 1
        val by2 = r1.pos.y + r1.height - 1

        var code = 0

        if (bx2 >= ax1 && bx1 <= ax2) {
            if (by1 > ay1 && by1 <= ay2) {
                code = code or TOP_SIDE
            }
            if (by2 >= ay1 && by2 < ay2) {
                code = code or BOTTOM_SIDE
            }
        }

        if (by2 >= ay1 && by1 <= ay2) {
            if (bx1 > ax1 && bx1 <= ax2) {
                code = code or LEFT_SIDE
            }
            if (bx2 >= ax1 && bx2 < ax2) {
                code = code or RIGHT_SIDE
            }
        }
        return code
    }

    fun intersection(r: Rectangle, r1: Rectangle): Rectangle {
        val x1 = Math.max(r.pos.x, r1.pos.x)
        val y1 = Math.max(r.pos.y, r1.pos.y)
        val x2 = Math.min(r.pos.x + r.width - 1, r1.pos.x + r1.width - 1)
        val y2 = Math.min(r.pos.y + r.height - 1, r1.pos.y + r1.height - 1)
        return Rectangle(x1, y1, Math.max(0, x2 - x1 + 1), Math.max(0, y2 - y1 + 1))
    }

    fun intersection(x1: Int, width1: Int, x2: Int, width2: Int): Int {
        return Math.max(0, Math.min(x1 + width1 - 1, x2 + width2 - 1) - Math.max(x1, x2) + 1)
    }

    fun intersection(r: Rectangle, r1: Rectangle, result: Rectangle): Rectangle {
        result.pos.x = Math.max(r.pos.x, r1.pos.x)
        result.pos.y = Math.max(r.pos.y, r1.pos.y)
        result.width = Math.max(0, Math.min(r.pos.x + r.width - 1, r1.pos.x + r1.width - 1) - result.pos.x + 1)
        result.height = Math.max(0, Math.min(r.pos.y + r.height - 1, r1.pos.y + r1.height - 1) - result.pos.y + 1)
        return result
    }

    fun union(r: Rectangle, r1: Rectangle): Rectangle {
        val x1 = Math.min(r.pos.x, r1.pos.x)
        val y1 = Math.min(r.pos.y, r1.pos.y)
        val x2 = Math.max(r.pos.x + r.width - 1, r1.pos.x + r1.width - 1)
        val y2 = Math.max(r.pos.y + r.height - 1, r1.pos.y + r1.height - 1)
        return Rectangle(x1, y1, x2 - x1 + 1, y2 - y1 + 1)
    }

    fun unionAdd(r: Rectangle, r1: Rectangle) {
        val x1 = Math.min(r.pos.x, r1.pos.x)
        val y1 = Math.min(r.pos.y, r1.pos.y)
        val x2 = Math.max(r.pos.x + r.width - 1, r1.pos.x + r1.width - 1)
        val y2 = Math.max(r.pos.y + r.height - 1, r1.pos.y + r1.height - 1)
        r.pos.x = x1
        r.pos.y = y1
        r.width = x2 - x1 + 1
        r.height = y2 - y1 + 1
    }

    fun getBoundary(r: Rectangle, side: Int): Int =
        when (side) {
            LEFT_SIDE -> r.pos.x
            TOP_SIDE -> r.pos.y
            RIGHT_SIDE -> r.pos.x + r.width - 1
            BOTTOM_SIDE -> r.pos.y + r.height - 1
            else -> r.pos.x
        }

    fun contains(r: Rectangle, x: Int, y: Int): Boolean {
        return x >= r.pos.x &&
            y >= r.pos.y &&
            x < r.pos.x + r.width &&
            y < r.pos.y + r.height
    }

    fun contains(r: Rectangle, p: Position): Boolean =
        contains(r, p.x, p.y)

    fun contains(r: Rectangle, r1: Rectangle): Boolean =
        r1.pos.x >= r.pos.x &&
        r1.pos.y >= r.pos.y &&
        r1.pos.x + r1.width <= r.pos.x + r.width &&
        r1.pos.y + r1.height <= r.pos.y + r.height

    fun getOppositeSide(side: Int): Int =
        when (side) {
            LEFT_SIDE ->  RIGHT_SIDE
            TOP_SIDE ->  BOTTOM_SIDE
            RIGHT_SIDE ->  LEFT_SIDE
            BOTTOM_SIDE ->  TOP_SIDE
            else ->  -1
        }

    fun setOutsideBoundary(r: Rectangle, side: Int, boundary: Int): Rectangle =
        when (side) {
            LEFT_SIDE -> {
                r.width += r.pos.x - boundary
                r.pos.x = boundary
                r
            }
            TOP_SIDE -> {
                r.height += r.pos.y - boundary
                r.pos.y = boundary
                r
            }
            RIGHT_SIDE -> {
                r.width = boundary - r.pos.x
                r
            }
            BOTTOM_SIDE -> {
                r.height = boundary - r.pos.y
                r
            }
            else -> {r}
        }



    fun rotateLeft(d: Direction): Direction =
        when (d) {
            NORTH -> NORTH_EAST
            NORTH_EAST -> EAST
            EAST -> SOUTH_EAST
            SOUTH_EAST -> SOUTH
            SOUTH -> SOUTH_WEST
            SOUTH_WEST -> WEST
            WEST -> NORTH_WEST
            NORTH_WEST -> NORTH
            else -> NONE
        }

    fun rotateLeft2(d: Direction): Direction =
        when (d) {
            NORTH -> EAST
            NORTH_EAST -> SOUTH_EAST
            EAST -> SOUTH
            SOUTH_EAST -> SOUTH_WEST
            SOUTH -> WEST
            SOUTH_WEST -> NORTH_WEST
            WEST -> NORTH
            NORTH_WEST -> NORTH_EAST
            else -> NONE
        }

    fun rotateRight(d: Direction): Direction =
        when (d) {
            NORTH -> NORTH_WEST
            NORTH_WEST -> WEST
            WEST -> SOUTH_WEST
            SOUTH_WEST -> SOUTH
            SOUTH -> SOUTH_EAST
            SOUTH_EAST -> EAST
            EAST -> NORTH_EAST
            NORTH_EAST -> NORTH
            else -> NONE
        }

    fun rotateRight2(d: Direction): Direction =
        when (d) {
            NORTH -> WEST
            NORTH_WEST -> SOUTH_WEST
            WEST -> SOUTH
            SOUTH_WEST -> SOUTH_EAST
            SOUTH -> EAST
            SOUTH_EAST -> NORTH_EAST
            EAST -> NORTH
            NORTH_EAST -> NORTH_WEST
            else -> NONE
        }

    fun isHorizontal(d: Direction): Boolean =
        d == EAST || d == WEST

    fun isVertical(d: Direction): Boolean =
        d == NORTH || d == SOUTH

    fun translateTo(p: Position, to: Position) {
        p.x = to.x
        p.y = to.y
    }

    fun translate(p: Position, d: Vector2i) {
        p.x += d.dx
        p.y += d.dy
    }

    fun getTranslatedXPos(p: Position, d: Direction, dx: Int = 1): Int =
        when (d.horizontal) {
            Orientation.WEST -> p.x - dx
            Orientation.EAST -> p.x + dx
            else -> p.x
        }

    fun getTranslatedYPos(p: Position, d: Direction, dy: Int = 1): Int =
        when (d.vertical) {
            Orientation.SOUTH -> p.y + dy
            Orientation.NORTH -> p.y - dy
            else -> p.y
        }

    fun movePosition(position: Position, d: Direction, distance: Int, originUpperCorner: Boolean) {
        movePosition(position, d.horizontal, distance, originUpperCorner)
        movePosition(position, d.vertical, distance, originUpperCorner)
    }

    fun bitMaskIntersection(source: BitSet, sourceRect: Rectangle, intersectionRect: Rectangle, result: BitSet) {
        result.clear()
        var y = 0
        var x = 0
        while (y < intersectionRect.height) {
            while (x < intersectionRect.width) {
                result.set(
                    getFlatArrayIndex(x, y, intersectionRect.width),
                    source.get((intersectionRect.pos.y + y) * sourceRect.width + intersectionRect.pos.x + x)
                )
                x++
            }
            y++
        }
    }

    fun getFlatArrayIndex(x: Int, y: Int, width: Int): Int =
        y * width + x

    fun movePosition(pos: Position, orientation: Orientation, distance: Int = 1, originUpperCorner: Boolean = true) =
        when (orientation) {
            Orientation.NORTH -> pos.y = if (originUpperCorner) pos.y - distance else pos.y + distance
            Orientation.SOUTH -> pos.y = if (originUpperCorner) pos.y + distance else pos.y - distance
            Orientation.WEST -> pos.x -= distance
            Orientation.EAST -> pos.x += distance
            else -> {}
        }
}
