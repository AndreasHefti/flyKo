package com.inari.firefly.graphics.rendering

import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.aspect.Aspects
import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.component.ComponentMap
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityActivationEvent
import com.inari.firefly.system.FFSystem
import com.inari.firefly.system.component.ComponentSystem

object RenderingSystem : FFSystem {

    @JvmField internal val _renderer = ComponentSystem.createComponentMapping(
        Renderer
    )
    val renderer: ComponentMap<Renderer> = _renderer

    @JvmField var allowMultipleAcceptance: Boolean = false
    private var renderingChain: Array<out Renderer> = emptyArray()

    init {
        FFContext.registerListener(
            FFApp.RenderEvent,
            object : FFApp.RenderEvent.Listener {
                override fun invoke(viewId: Int, layerId: Int, clip: Rectangle) {
                    var i = 0
                    while (i < renderingChain.size) {
                        renderingChain[i++].render(viewId, layerId, clip)
                    }
                }
            }
        )

        FFContext.registerListener(
            EntityActivationEvent,
            object : EntityActivationEvent.Listener {
                override fun entityActivated(entity: Entity) {
                    var i = 0
                    while (i < renderingChain.size) {
                        val renderer = renderingChain[i++]
                        if (renderer.match(entity)) {
                            if (renderer.accept(entity) && !allowMultipleAcceptance)
                                return
                        }
                    }
                }

                override fun entityDeactivated(entity: Entity) {
                    var i = 0
                    while (i < renderingChain.size) {
                        val renderer = renderingChain[i++]
                        if (renderer.match(entity))
                            renderer.dispose(entity)

                    }
                }

                override fun match(aspects: IAspects) = true
            }
        )

        setDefaultRenderingChain()
    }

    fun setDefaultRenderingChain() {
        setRenderingChain(
            SimpleTileGridRenderer,
            MultiPositionSpriteRenderer,
            SimpleSpriteRenderer,
            // TODO
            SimpleShapeRenderer,
            SimpleTextRenderer
        )
    }

    fun setRenderingChain(vararg renderingChain: Renderer) {
        _renderer.clear()
        this.renderingChain = renderingChain
        var i = 0
        while (i < RenderingSystem.renderingChain.size) {
            val renderer = RenderingSystem.renderingChain[i++]
            _renderer.receiver()(renderer)
        }
    }

    override fun clearSystem() {}

}