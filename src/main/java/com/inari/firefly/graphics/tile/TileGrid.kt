package com.inari.firefly.graphics.tile

import com.inari.commons.GeomUtils
import com.inari.commons.geom.Direction
import com.inari.commons.geom.Orientation.*
import com.inari.util.geom.Position
import com.inari.util.geom.PositionF
import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.IntIterator
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.graphics.rendering.Renderer
import com.inari.firefly.graphics.view.Layer
import com.inari.firefly.graphics.view.View
import com.inari.firefly.graphics.view.ViewLayerAware
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent
import java.util.*


class TileGrid private constructor() : SystemComponent(), ViewLayerAware {



    @JvmField internal var viewRef = -1
    @JvmField internal var layerRef = -1
    @JvmField internal var rendererRef = -1

    val ff_View = ComponentRefResolver(View, { index-> viewRef = index })
    val ff_Layer = ComponentRefResolver(Layer, { index-> layerRef = index })
    var ff_Renderer = ComponentRefResolver(Renderer, { index-> rendererRef = index })
    var ff_GridWidth: Int = -1
        set(value) {field = setIfNotInitialized(value, "ff_GridWidth")}
    var ff_GridHeight: Int = -1
        set(value) {field = setIfNotInitialized(value, "ff_GridHeight")}
    var ff_CellWidth: Int = -1
        set(value) {field = setIfNotInitialized(value, "ff_CellWidth")}
    var ff_CellHeight: Int = -1
        set(value) {field = setIfNotInitialized(value, "ff_CellHeight")}
    val ff_Position: PositionF = PositionF(0.0f, 0.0f)
    var ff_Spherical: Boolean = false
        set(value) {field = setIfNotInitialized(value, "ff_Spherical")}

    override val viewIndex: Int
        get() = viewRef
    override val layerIndex: Int
        get() = layerRef

    @JvmField internal var grid: Array<IntArray> = Array(0, { IntArray(0) })
    @JvmField internal val normalisedWorldBounds = Rectangle(0, 0, 0, 0)

    override fun init() {
        grid = Array(ff_GridHeight, { IntArray(ff_GridWidth, { _ -> -1 }) })
        normalisedWorldBounds.width = ff_GridWidth
        normalisedWorldBounds.height = ff_GridHeight

        super.init()
    }

    operator fun get(ypos: Int, xpos: Int): Int =
        if (ff_Spherical)
            grid[ypos % ff_GridHeight][xpos % ff_GridWidth]
         else
            grid[ypos][xpos]

    fun getTileAt(worldPos: Position): Int =
        get(Math.floor((worldPos.x.toDouble() - ff_Position.x) / ff_CellWidth).toInt(),
            Math.floor((worldPos.y.toDouble() - ff_Position.y) / ff_CellHeight).toInt())

    fun getTileAt(xpos: Float, ypos: Float): Int =
        get(Math.floor((xpos.toDouble() - ff_Position.x) / ff_CellWidth).toInt(),
            Math.floor((ypos.toDouble() - ff_Position.y) / ff_CellHeight).toInt())

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
        var _xpos = xpos
        var _ypos = ypos
        if (ff_Spherical) {
            _xpos = xpos % ff_GridWidth
            _ypos = ypos % ff_GridHeight
        }
        if (grid[_ypos][_xpos] == entityId) {
            grid[_ypos][_xpos] = -1
        }
    }

    fun getNeighbour(xpos: Int, ypos: Int, direction: Direction): Int {
        return getNeighbour(xpos, ypos, direction, 1, 1)
    }

    fun getNeighbour(xpos: Int, ypos: Int, direction: Direction, xDistance: Int, yDistance: Int): Int =
        get(
            when (direction.horizontal) {
                WEST ->  xpos - xDistance
                EAST ->  xpos + xDistance
                else -> xpos
            },
            when (direction.vertical) {
                NORTH -> ypos + yDistance
                SOUTH -> ypos + yDistance
                else -> ypos
            }
        )

    val tileGridIterator: TileGridIterator
        get() = TileGridIterator.getInstance(this)

    fun tileGridIterator(worldClip: Rectangle): TileGridIterator =
        TileGridIterator.getInstance(worldClip, this)


    override fun indexedTypeKey() = typeKey
    companion object : SingleType<TileGrid>() {
        override val typeKey = SystemComponent.createTypeKey(TileGrid::class.java)
        override fun createEmpty() = TileGrid()
    }


    class TileGridIterator private constructor() : IntIterator {
        private val NULL_TILE_GRID = TileGrid()
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
        override fun next(): Int {
            val result = tileGrid[clip.y, clip.x]
            calcWorldPosition()
            clip.x++
            findNext()
            return result
        }

        private fun reset(tileGrid: TileGrid) {
            clip.x = 0
            clip.y = 0
            clip.width = tileGrid.ff_GridWidth
            clip.height = tileGrid.ff_GridHeight
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
            tmpClip.x = Math.floor((worldClip.x.toDouble() - tileGrid.ff_Position.x) / tileGrid.ff_CellWidth).toInt()
            tmpClip.y = Math.floor((worldClip.y.toDouble() - tileGrid.ff_Position.y) / tileGrid.ff_CellHeight).toInt()
            val x2 = Math.ceil((worldClip.x.toDouble() - tileGrid.ff_Position.x + worldClip.width) / tileGrid.ff_CellWidth).toInt()
            val y2 = Math.ceil((worldClip.y.toDouble() - tileGrid.ff_Position.y + worldClip.height) / tileGrid.ff_CellHeight).toInt()
            tmpClip.width = x2 - tmpClip.x
            tmpClip.height = y2 - tmpClip.y
            GeomUtils.intersection(tmpClip, tileGrid.normalisedWorldBounds, result)
        }

        @Suppress("NOTHING_TO_INLINE")
        private inline fun findNext() {
            while (clip.y < ysize) {
                while (clip.x < xsize) {
                    if (tileGrid[clip.y, clip.x] != -1) {
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
            worldPosition.x = tileGrid.ff_Position.x + clip.x * tileGrid.ff_CellWidth
            worldPosition.y = tileGrid.ff_Position.y + clip.y * tileGrid.ff_CellHeight
        }

        companion object {

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
                        POOL.pollLast()
                }
        }
    }
}