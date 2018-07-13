package com.inari.firefly.graphics.rendering

import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.sprite.EMultiplier
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.system.component.SingletonComponent
import com.inari.util.geom.Rectangle

class MultiPositionSpriteRenderer private constructor() : Renderer() {

    override fun match(entity: Entity): Boolean =
        entity.aspects.include(MATCHING_ASPECTS)

    override fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle) {
        val toRender = getIfNotEmpty(viewIndex, layerIndex) ?: return

        val graphics = FFContext.graphics
        var i = 0
        while (i < toRender.capacity) {
            val entity = toRender[i++] ?: continue
            val sprite = entity[ESprite]
            val transform = entity[ETransform]
            val multiplier = entity[EMultiplier]
            transformCollector(transform.data)

            var j = 0
            val positions = multiplier.positions
            while (j < positions.capacity) {
                val pos = positions[j++] ?: continue
                transformCollector(pos)
                graphics.renderSprite(sprite, transformCollector.data)
            }
        }
    }

    companion object : SingletonComponent<Renderer, MultiPositionSpriteRenderer>(Renderer, MultiPositionSpriteRenderer::class.java) {
        override fun create() = MultiPositionSpriteRenderer()
        private val MATCHING_ASPECTS = EntityComponent.ENTITY_COMPONENT_ASPECTS.createAspects(
            ETransform, ESprite, EMultiplier
        )
    }
}