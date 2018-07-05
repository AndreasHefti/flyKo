package com.inari.firefly.graphics.rendering

import com.inari.firefly.FFContext
import com.inari.firefly.entity.EChild
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.graphics.tile.ETile
import com.inari.firefly.system.component.SingletonComponent
import com.inari.util.geom.Rectangle

class SimpleSpriteRenderer private constructor() : Renderer() {



    override fun match(entity: Entity): Boolean =
        entity.components.include(MATCHING_ASPECTS) &&
            entity.components.exclude(EXCLUDING_ASPECTS)

    override fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle) {
        val toRender = getIfNotEmpty(viewIndex, layerIndex) ?: return

        val graphics = FFContext.graphics
        var i = 0
        while (i < toRender.capacity()) {
            val entity = toRender.get(i++) ?: continue
            graphics.renderSprite(
                entity[ESprite],
                entity[ETransform].data
            )
        }
    }

    companion object : SingletonComponent<SimpleSpriteRenderer, Renderer>() {
        override val indexedTypeKey = Renderer.indexedTypeKey
        override val subType = SimpleSpriteRenderer::class.java
        override fun create() = SimpleSpriteRenderer()

        private val MATCHING_ASPECTS = EntityComponent.ENTITY_COMPONENT_ASPECTS.createAspects(
            ETransform, ESprite
        )
        private val EXCLUDING_ASPECTS = EntityComponent.ENTITY_COMPONENT_ASPECTS.createAspects(
            EChild, ETile
        )
    }
}