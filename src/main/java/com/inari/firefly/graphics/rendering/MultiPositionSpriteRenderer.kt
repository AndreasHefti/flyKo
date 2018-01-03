package com.inari.firefly.graphics.rendering

import com.inari.commons.geom.Rectangle
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.sprite.EMultiplier
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.system.component.SingletonComponent

class MultiPositionSpriteRenderer private constructor() : Renderer() {

    private val matchingAspects = EntityComponent.ENTITY_COMPONENT_ASPECTS.createAspects(
        ETransform, ESprite, EMultiplier
    )

    override fun match(entity: Entity): Boolean =
        entity.aspects.include(matchingAspects)

    override fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle) {
        val toRender = getIfNotEmpty(viewIndex, layerIndex) ?: return

        val graphics = FFContext.graphics
        var i = 0
        while (i < toRender.capacity()) {
            val entity = toRender.get(i++) ?: continue
            val sprite = entity[ESprite]
            val transform = entity[ETransform]
            val multiplier = entity[EMultiplier]
            transformCollector.set(transform)

            var j = 0
            val positions = multiplier.positions
            while (j < positions.capacity()) {
                val pos = positions[j++] ?: continue
                transformCollector.set(pos)
                graphics.renderSprite(sprite, transformCollector)
            }
        }
    }

    companion object : SingletonComponent<MultiPositionSpriteRenderer, Renderer>() {
        override val typeKey = Renderer.typeKey
        override fun subType() = MultiPositionSpriteRenderer::class.java
        override fun create() = MultiPositionSpriteRenderer()
    }
}