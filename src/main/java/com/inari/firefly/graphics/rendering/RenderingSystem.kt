package com.inari.firefly.graphics.rendering

import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.aspect.Aspects
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.component.ComponentMap
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityActivationEvent
import com.inari.firefly.system.FFSystem
import com.inari.firefly.system.component.ComponentSystem

object RenderingSystem : FFSystem {

    @JvmField val DEFAULT_RENDERING_CHAIN: List<Renderer> =
        List(3, { index -> when(index) {
            0 -> SimpleTileGridRenderer
            1 -> MultiPositionSpriteRenderer
            2 -> SimpleSpriteRenderer
            // TODO
            4 -> SimpleShapeRenderer
            5 -> SimpleTextRenderer
            else -> {throw IllegalStateException()}
        }})

    @JvmField internal val _renderer = ComponentSystem.createComponentMapping(
        Renderer
    )
    val renderer: ComponentMap<Renderer> = _renderer

    var ff_AllowMultipleAcceptance: Boolean = false
    private var renderingChain: Array<Renderer> = DEFAULT_RENDERING_CHAIN.toTypedArray()

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
                        val renderer = renderingChain[i]
                        if (renderer.match(entity)) {
                            if (renderer.accept(entity) && !ff_AllowMultipleAcceptance) {
                                return
                            }
                        }
                        i++
                    }
                }

                override fun entityDeactivated(entity: Entity) {
                    var i = 0
                    while (i < renderingChain.size) {
                        val renderer = renderingChain[i]
                        if (renderer.match(entity)) {
                            renderer.dispose(entity)
                        }
                        i++
                    }
                }

                override fun match(aspects: Aspects) = true
            }
        )
    }

    fun setRenderingChain(renderingChain: List<Renderer>) {
        this.renderingChain = renderingChain.toTypedArray()
    }

    override fun clearSystem() {}

}