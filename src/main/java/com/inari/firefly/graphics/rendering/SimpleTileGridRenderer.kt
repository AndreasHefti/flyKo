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

class SimpleTileGridRenderer private constructor() : Renderer() {

    private val matchingAspects = EntityComponent.ENTITY_COMPONENT_ASPECTS.createAspects(
        ETransform, ETile
    )

    override fun match(entity: Entity): Boolean =
        entity.components.include(matchingAspects)

    override fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle) {
        val tileGrid = TileGridSystem[viewIndex, layerIndex] ?: return
        if (tileGrid.rendererRef < 0 || tileGrid.rendererRef == index) {
            val graphics = FFContext.graphics
            val iterator = tileGrid.tileGridIterator(clip)
            while (iterator.hasNext()) {
                graphics.renderSprite(
                    EntitySystem.entities[iterator.next()][ETile],
                    iterator.worldXPos,
                    iterator.worldYPos
                )
            }
        }
    }

    companion object : SingletonComponent<SimpleTileGridRenderer, Renderer>() {
        override val typeKey = Renderer.typeKey
        override fun subType() = SimpleTileGridRenderer::class.java
        override fun create() = SimpleTileGridRenderer()
    }
}