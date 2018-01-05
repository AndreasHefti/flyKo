package com.inari.firefly.graphics.rendering

import com.inari.commons.geom.Rectangle
import com.inari.firefly.FFContext
import com.inari.firefly.entity.EChild
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.system.component.SingletonComponent


class SpriteGroupRenderer private constructor() : Renderer() {

    override fun match(entity: Entity): Boolean =
        entity.aspects.include(MATCHING_ASPECTS)

    override fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle) {
        val toRender = getIfNotEmpty(viewIndex, layerIndex) ?: return

        val graphics = FFContext.graphics
        var i = 0
        while (i < toRender.capacity()) {
            val entity = toRender.get(i++) ?: continue

            val sprite = entity[ESprite]
            val transform = entity[ETransform]
            val group = entity[EChild]

            transformCollector.set(transform)
            collectTransformData(group.parent, transformCollector)
            graphics.renderSprite(sprite, transformCollector)
        }
    }

    private fun collectTransformData(parentId: Int, transformCollector: TransformDataCollector) {
        if (parentId < 0)
            return

        val parent = EntitySystem[parentId]
        val parentTransform = parent[ETransform]
        transformCollector.add(parentTransform)
        if (EChild in parent.aspects)
            collectTransformData(parent[EChild].parent, transformCollector)
    }

    private val COMPARATOR = Comparator<Entity> { e1, e2 ->
        if (e1 == null && e2 == null)
            return@Comparator 0
        if (e1 == null)
            return@Comparator 1
        if (e2 == null)
            return@Comparator -1

        val p1 = e1[EChild].zpos
        val p2 = e2[EChild].zpos
        when {
            p1 == p2 -> 0
            p1 < p2 -> -1
            else -> 1
        }
    }

    companion object : SingletonComponent<SpriteGroupRenderer, Renderer>() {
        override val typeKey = Renderer.typeKey
        override fun subType() = SpriteGroupRenderer::class.java
        override fun create() = SpriteGroupRenderer()

        private val MATCHING_ASPECTS = EntityComponent.ENTITY_COMPONENT_ASPECTS.createAspects(
            ETransform, ESprite, EChild
        )
    }
}