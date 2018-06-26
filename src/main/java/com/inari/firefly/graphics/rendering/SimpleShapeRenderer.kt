package com.inari.firefly.graphics.rendering

import com.inari.commons.geom.Rectangle
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.shape.EShape
import com.inari.firefly.system.component.SingletonComponent

class SimpleShapeRenderer private constructor() : Renderer() {

    override fun match(entity: Entity): Boolean =
        EShape in entity.aspects

    override fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle) {
        val toRender = getIfNotEmpty(viewIndex, layerIndex) ?: return

        val graphics = FFContext.graphics
        var i = 0
        while (i < toRender.capacity()) {
            val entity = toRender.get(i++) ?: continue
            graphics.renderShape(
                entity[EShape].data,
                entity[ETransform].data
            )
        }
    }

    companion object : SingletonComponent<SimpleShapeRenderer, Renderer>() {
        override val typeKey = Renderer.typeKey
        override fun subType() = SimpleShapeRenderer::class.java
        override fun create() = SimpleShapeRenderer()
    }
}