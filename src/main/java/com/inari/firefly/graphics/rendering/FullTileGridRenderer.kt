package com.inari.firefly.graphics.rendering

import com.inari.commons.geom.Rectangle
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.tile.ETile
import com.inari.firefly.graphics.tile.TileGridSystem
import com.inari.firefly.system.component.SingletonComponent

class FullTileGridRenderer private constructor() : Renderer() {

    override fun match(entity: Entity): Boolean =
        entity.components.include(MATCHING_ASPECTS)

    override fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle) {
        val tileGrid = TileGridSystem[viewIndex, layerIndex] ?: return

        if (tileGrid.rendererRef < 0 || tileGrid.rendererRef == index) {
            val graphics = FFContext.graphics
            val iterator = tileGrid.tileGridIterator(clip)
            while (iterator.hasNext()) {
                val entity = EntitySystem.entities[iterator.next()]

                transformCollector.set(entity[ETransform])
                transformCollector.addOffset(iterator.worldXPos, iterator.worldYPos)
                graphics.renderSprite(entity[ETile], transformCollector)
            }
        }
    }

    companion object : SingletonComponent<FullTileGridRenderer, Renderer>() {
        override val typeKey = Renderer.typeKey
        override fun subType() = FullTileGridRenderer::class.java
        override fun create() = FullTileGridRenderer()

        private val MATCHING_ASPECTS = EntityComponent.ENTITY_COMPONENT_ASPECTS.createAspects(
            ETransform, ETile
        )
    }
}