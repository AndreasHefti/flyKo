package com.inari.firefly.graphics.rendering

import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityActivationEvent
import com.inari.firefly.system.FFSystem
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SingletonComponent
import com.inari.util.aspect.Aspects
import com.inari.util.geom.Rectangle

object RenderingSystem : FFSystem {

    @JvmField internal val renderer =
        ComponentSystem.createComponentMapping(Renderer)

    @JvmField var allowMultipleAcceptance: Boolean = false
    private var renderingChain: Array<Renderer> = emptyArray()

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

                override fun match(aspects: Aspects) = true
            }
        )

        setDefaultRenderingChain()
    }

    operator fun get(rendererKey: SingletonComponent<out Renderer, Renderer>): Renderer =
        renderer[rendererKey.instance.index]

    fun setDefaultRenderingChain() {
        setRenderingChain(
            SimpleTileGridRenderer,
            MultiPositionSpriteRenderer,
            SimpleSpriteRenderer,
            SpriteGroupRenderer,
            SimpleShapeRenderer,
            SimpleTextRenderer
        )
    }

    fun setRenderingChain(vararg renderingChain: SingletonComponent<out Renderer, Renderer>) {
        renderer.clear()
        this.renderingChain = Array(renderingChain.size) {
            val r: Renderer = renderingChain[it].instance
            renderer.receiver()(r)
            r
        }
    }

    override fun clearSystem() {}

}