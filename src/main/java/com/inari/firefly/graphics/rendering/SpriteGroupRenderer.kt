package com.inari.firefly.graphics.rendering

import com.inari.commons.geom.Rectangle
import com.inari.firefly.FFContext
import com.inari.firefly.entity.EChild
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.sprite.ESprite


object SpriteGroupRenderer : Renderer() {

    private val matchingAspects = EntityComponent.ENTITY_COMPONENT_ASPECTS.createAspects(
        ETransform, ESprite, EChild
    )

    override fun match(entity: Entity): Boolean =
        entity.aspects.include(matchingAspects)

    override fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle) {
        if (!contains(viewIndex, layerIndex))
            return

        val graphics = FFContext.graphics
        val spritesToRender = get(viewIndex, layerIndex)
        var i = 0
        while (i < spritesToRender.capacity()) {
            val entity = spritesToRender.get(i++) ?: continue

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
}