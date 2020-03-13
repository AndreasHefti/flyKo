package com.inari.firefly.graphics.tile

import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.graphics.rendering.Renderer
import com.inari.firefly.graphics.view.Layer
import com.inari.firefly.graphics.view.View
import com.inari.firefly.graphics.view.ViewLayerAware
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentSingleType
import com.inari.java.types.ArrayDeque
import com.inari.util.geom.*
import kotlin.math.ceil
import kotlin.math.floor


class TileGrid private constructor() : SystemComponent(TileGrid::class.java.name), ViewLayerAware {

    @JvmField internal var viewRef = -1
    @JvmField internal var layerRef = -1
    @JvmField internal var rendererRef = -1
    @JvmField internal val gridDim = Vector2i(-1, -1)
    @JvmField internal val cellDim = Vector2i(-1, -1)
    @JvmField internal val position = PositionF(0.0f, 0.0f)
    @JvmField internal var spherical = false

    val ff_View = ComponentRefResolver(View) { index-> viewRef = index }
    val ff_Layer = ComponentRefResolver(Layer) { index-> layerRef = index }
    var ff_Renderer = ComponentRefResolver(Renderer) { index-> rendererRef = index }
    var ff_GridWidth: Int
        get() = gridDim.dx
        set(value) {gridDim.dx = setIfNotInitialized(value, "ff_GridWidth")}
    var ff_GridHeight: Int
        get() = gridDim.dy
        set(value) {gridDim.dy = setIfNotInitialized(value, "ff_GridHeight")}
    var ff_CellWidth: Int
        get() = cellDim.dx
        set(value) {cellDim.dx = setIfNotInitialized(value, "ff_CellWidth")}
    var ff_CellHeight: Int
        get() = cellDim.dy
        set(value) {cellDim.dy = setIfNotInitialized(value, "ff_CellHeight")}
    var ff_Position: PositionF
        get() = position
        set(value) = position(value)
    var ff_Spherical: Boolean
        get() = spherical
        set(value) {spherical = setIfNotInitialized(value, "ff_Spherical")}

    override val viewIndex: Int
        get() = viewRef
    override val layerIndex: Int
        get() = layerRef

    @JvmField internal var grid: Array<IntArray> = Array(0) { IntArray(0) }
    @JvmField internal val normalisedWorldBounds = Rectangle(0, 0, 0, 0)

    override fun init() {
        grid = Array(ff_GridHeight) { IntArray(ff_GridWidth) { -1 } }
        normalisedWorldBounds.width = ff_GridWidth
        normalisedWorldBounds.height = ff_GridHeight

        super.init()
    }

    operator fun get(rectPos: Rectangle): Int =
        this[rectPos.pos]

    operator fun get(pos: Position): Int =
        if (ff_Spherical)
            grid[pos.y % ff_GridHeight][pos.x % ff_GridWidth]
        else
            grid[pos.y][pos.x]

    operator fun get(xpos: Int, ypos: Int): Int =
        if (ff_Spherical)
            grid[ypos % ff_GridHeight][xpos % ff_GridWidth]
         else
            grid[ypos][xpos]

    fun getTileAt(worldPos: Position): Int =
        get(floor((worldPos.x.toDouble() - ff_Position.x) / ff_CellWidth).toInt(),
            floor((worldPos.y.toDouble() - ff_Position.y) / ff_CellHeight).toInt())

    fun getTileAt(xpos: Float, ypos: Float): Int =
        get(floor((xpos.toDouble() - ff_Position.x) / ff_CellWidth).toInt(),
            floor((ypos.toDouble() - ff_Position.y) / ff_CellHeight).toInt())

    operator fun set(position: Position, entityId: Int) =
        set(entityId, position.x, position.y)

    operator fun set(ypos: Int, xpos: Int, entityId: Int) =
        if (ff_Spherical)
            grid[ypos % ff_GridHeight][xpos % ff_GridWidth] = entityId
        else
            grid[ypos][xpos] = entityId

    fun reset(xpos: Int, ypos: Int): Int {
        return if (ff_Spherical) {
            val old = grid[ypos % ff_GridHeight][xpos % ff_GridWidth]
            grid[ypos % ff_GridHeight][xpos % ff_GridWidth] = -1
            old
        } else {
            val old = grid[ypos][xpos]
            grid[ypos][xpos] = -1
            old
        }
    }

    fun resetIfMatch(entityId: Int, position: Position) =
        resetIfMatch(entityId, position.x, position.y)

    fun resetIfMatch(entityId: Int, xpos: Int, ypos: Int) {
        var ixpos = xpos
        var iypos = ypos
        if (ff_Spherical) {
            ixpos = xpos % ff_GridWidth
            iypos = ypos % ff_GridHeight
        }
        if (grid[iypos][ixpos] == entityId) {
            grid[iypos][ixpos] = -1
        }
    }

    fun getNeighbour(xpos: Int, ypos: Int, direction: Direction): Int {
        return getNeighbour(xpos, ypos, direction, 1, 1)
    }

    fun getNeighbour(xpos: Int, ypos: Int, direction: Direction, xDistance: Int, yDistance: Int): Int =
        get(
            when (direction.horizontal) {
                Orientation.WEST ->  xpos - xDistance
                Orientation.EAST ->  xpos + xDistance
                else -> xpos
            },
            when (direction.vertical) {
                Orientation.NORTH -> ypos + yDistance
                Orientation.SOUTH -> ypos + yDistance
                else -> ypos
            }
        )

    val tileGridIterator: TileGridIterator
        get() = TileGridIterator.getInstance(this)

    fun tileGridIterator(worldClip: Rectangle): TileGridIterator =
        TileGridIterator.getInstance(worldClip, this)

    override fun componentType() = Companion

    companion object : SystemComponentSingleType<TileGrid>(TileGrid::class.java) {
        override fun createEmpty() = TileGrid()
    }


    class TileGridIterator private constructor() : IntIterator() {

        @JvmField internal val tmpClip = Rectangle()
        @JvmField internal val worldPosition = PositionF()
        @JvmField internal val clip = Rectangle()
        @JvmField internal var xorig: Int = 0
        @JvmField internal var xsize: Int = 0
        @JvmField internal var ysize: Int = 0
        @JvmField internal var tileGrid: TileGrid = NULL_TILE_GRID
        @JvmField internal var hasNext: Boolean = false

        val worldXPos: Float get() = worldPosition.x
        val worldYPos: Float get() = worldPosition.y

        override fun hasNext(): Boolean = hasNext
        override fun nextInt(): Int {
            val result = tileGrid[clip]
            calcWorldPosition()
            clip.x++
            findNext()
            return result
        }

        private fun reset(tileGrid: TileGrid) {
            clip(0, 0, tileGrid.gridDim.dx, tileGrid.gridDim.dy)
            init(tileGrid)
        }

        private fun reset(clip: Rectangle, tileGrid: TileGrid) {
            mapWorldClipToTileGridClip(clip, tileGrid, this.clip)
            init(tileGrid)
        }

        private fun init(tileGrid: TileGrid) {
            xorig = clip.x
            xsize = clip.x + clip.width
            ysize = clip.y + clip.height

            this.tileGrid = tileGrid

            findNext()
        }

        private fun mapWorldClipToTileGridClip(worldClip: Rectangle, tileGrid: TileGrid, result: Rectangle) {
            tmpClip(
                floor((worldClip.x.toDouble() - tileGrid.position.x) / tileGrid.cellDim.dx).toInt(),
                floor((worldClip.y.toDouble() - tileGrid.position.y) / tileGrid.cellDim.dy).toInt()
            )
            val x2 = ceil((worldClip.x.toDouble() - tileGrid.position.x + worldClip.width) / tileGrid.cellDim.dx).toInt()
            val y2 = ceil((worldClip.y.toDouble() - tileGrid.position.y + worldClip.height) / tileGrid.cellDim.dy).toInt()
            tmpClip.width = x2 - tmpClip.x
            tmpClip.height = y2 - tmpClip.y
            GeomUtils.intersection(tmpClip, tileGrid.normalisedWorldBounds, result)
        }

        @Suppress("NOTHING_TO_INLINE")
        private inline fun findNext() {
            while (clip.y < ysize) {
                while (clip.x < xsize) {
                    if (tileGrid[clip] != -1) {
                        hasNext = true
                        return
                    }
                    clip.x++
                }
                clip.x = xorig
                clip.y++
            }
            dispose()
        }

        private fun dispose() {
            hasNext = false
            tileGrid = NULL_TILE_GRID
            xorig = -1
            xsize = -1
            ysize = -1
            POOL.add(this)
        }

        @Suppress("NOTHING_TO_INLINE")
        private inline fun calcWorldPosition() {
            worldPosition(
                tileGrid.position.x + clip.x * tileGrid.cellDim.dx,
                tileGrid.position.y + clip.y * tileGrid.cellDim.dy
            )
        }

        companion object {

            private val NULL_TILE_GRID: TileGrid  = TileGrid()
            private val POOL = ArrayDeque<TileGridIterator>(5)

            internal fun getInstance(clip: Rectangle, tileGrid: TileGrid): TileGridIterator {
                val instance = instance

                instance.reset(clip, tileGrid)
                return instance
            }

            internal fun getInstance(tileGrid: TileGrid): TileGridIterator {
                val instance = instance

                instance.reset(tileGrid)
                return instance
            }

            private val instance: TileGridIterator
                get() {
                    return if (POOL.isEmpty())
                        TileGridIterator()
                     else
                        POOL.pollLast()!!
                }
        }
    }
}