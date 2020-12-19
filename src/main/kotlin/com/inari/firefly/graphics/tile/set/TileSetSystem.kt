package com.inari.firefly.graphics.tile.set

import com.inari.firefly.FFContext
import com.inari.firefly.graphics.view.Layer

object TileSetSystem {

    private var mapping: MappingNode? = null

    internal fun activated(tileset: TileSet) {
        if (mapping == null)
            mapping = MappingNode(tileset.index)
        else
            mapping!!.add(tileset.index)
    }

    internal fun deactivated(tileset: TileSet) {
        mapping = mapping!!.remove(tileset.index)
    }

    operator fun get(tileSetName: String): TileSet =
        FFContext[TileSet, tileSetName]

    operator fun get(tileSetId: Int): TileSet =
            FFContext[TileSet, tileSetId]

    fun getEntityId(tileIndex: Int, layer: Layer): Int =
            getEntityId(tileIndex, layer.index)

    fun getEntityId(tileIndex: Int, layerId: Int): Int =
            mapping?.getTileEntityId(tileIndex, layerId) ?: -1



    private class MappingNode constructor(
        val tilesetId: Int
    ) {

        private var nextNode: MappingNode? = null
        private var offset: Int = 0
        private val size: Int = TileSetSystem[tilesetId].tiles.size

        fun add(tilesetId: Int) {
            if (nextNode == null)
                nextNode = MappingNode(tilesetId).updateOffset(this)
            else
                nextNode!!.add(tilesetId)
        }

        fun remove(tilesetId: Int, prevNode: MappingNode? = null): MappingNode? {
            val remove = this.tilesetId == tilesetId
            if (remove) {
                if (nextNode != null) {
                    if (prevNode != null)
                        prevNode.nextNode = nextNode

                    nextNode!!.updateOffset(prevNode)
                }

                val next = this.nextNode
                this.nextNode = null
                return next
            }

            return this
        }

        private fun updateOffset(prevNode: MappingNode? = null): MappingNode {
            if (prevNode == null)
                this.offset = 0
            else
                this.offset = prevNode.offset + prevNode.size

            return this
        }

        fun getTileEntityId(mappingId: Int, layerId: Int) : Int =
            when {
                mappingId < offset + size -> TileSetSystem[tilesetId][mappingId - offset, layerId]
                nextNode != null -> nextNode!!.getTileEntityId(mappingId, layerId)
                else -> -1
            }
    }
}