package com.inari.firefly.graphics.rendering

import com.inari.commons.geom.Rectangle
import com.inari.firefly.FFContext
import com.inari.firefly.entity.EChild
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.exclude
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.graphics.tile.ETile

object SimpleSpriteRenderer : Renderer() {

    private val matchingAspects = EntityComponent.ENTITY_ASPECTS.createAspects(
        ETransform, ESprite
    )
    private val excludingAspects = EntityComponent.ENTITY_ASPECTS.createAspects(
        EChild, ETile
    )

    override fun match(entity: Entity): Boolean =
        entity.components.include(matchingAspects) &&
            entity.components.exclude(excludingAspects)

    override fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle) {
        val toRender = this[viewIndex, layerIndex]
        if (toRender.isEmpty) {
            return
        }

        val graphics = FFContext.graphics
        var i = 0
        while (i < toRender.capacity()) {
            val entity = toRender.get(i++) ?: continue
            graphics.renderSprite(
                entity[ESprite],
                entity[ETransform]
            )
        }
    }
}