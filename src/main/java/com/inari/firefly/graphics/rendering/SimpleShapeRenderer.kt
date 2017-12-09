package com.inari.firefly.graphics.rendering

import com.inari.commons.geom.Rectangle
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.shape.EShape

object SimpleShapeRenderer : Renderer() {

    override fun match(entity: Entity): Boolean =
        EShape in entity.aspects

    override fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle) {
        val toRender = this[viewIndex, layerIndex]
        if (toRender.isEmpty) {
            return
        }

        val graphics = FFContext.graphics
        var i = 0
        while (i < toRender.capacity()) {
            val entity = toRender.get(i++) ?: continue
            graphics.renderShape(
                entity[EShape],
                entity[ETransform]
            )
        }
    }
}