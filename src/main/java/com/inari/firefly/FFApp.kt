package com.inari.firefly

import com.inari.commons.event.IEventDispatcher
import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.list.DynArray
import com.inari.commons.lang.list.DynArrayRO
import com.inari.firefly.external.*
import com.inari.firefly.graphics.view.ViewSystem
import com.inari.firefly.system.FFEvent

abstract class FFApp protected constructor(
    eventDispatcher: () -> IEventDispatcher,
    graphics: () -> FFGraphics,
    audio: () -> FFAudio,
    input: () -> FFInput,
    timer: () -> FFTimer
) {

    private val NO_VIRTUAL_VIEW_PORTS: DynArrayRO<ViewPortData> = DynArray.create(ViewPortData::class.java)
    private var disposed: Boolean = true

    init {
        if (initialized) {
            throw IllegalStateException("FFApp is a conceptual singleton and is already initialized")
        }

        Companion.eventDispatcher = eventDispatcher()
        Companion.graphics = graphics()
        Companion.audio = audio()
        Companion.input = input()
        Companion.timer = timer()

        disposed = false
    }

    fun dispose() {
        // TODO ?
        //FFContext.dispose()
        disposed = true
    }

    fun update() {
        FFApp.timer.tick()
        FFContext.notify(UpdateEvent)
        FFApp.timer.updateSchedulers()
    }

    fun render() {
        if (disposed)
            return

        val size = ViewSystem.activeViewPorts.size()
        if (size > 0) {
            var i = 0
            while (i < size) {
                val view = ViewSystem.activeViewPorts.get(i++)
                if (!ViewSystem.views.isActive(view.index)) {
                    continue
                }
                render(view)
            }

            graphics.flush(ViewSystem.activeViewPorts)
        } else {
            render(ViewSystem.baseView)
            graphics.flush(NO_VIRTUAL_VIEW_PORTS)
        }

        // TODO
        //FFContext.notify(postRenderEvent)
    }

    private fun render(view: ViewPortData) {
        RenderEvent.viewIndex = view.index
        RenderEvent.layerIndex = 0
        RenderEvent.clip.x = Math.floor(view.worldPosition.x.toDouble()).toInt()
        RenderEvent.clip.y = Math.floor(view.worldPosition.y.toDouble()).toInt()
        RenderEvent.clip.width = view.bounds.width
        RenderEvent.clip.height = view.bounds.height

        graphics.startRendering(view, true)

        val layersOfView = ViewSystem.layersOfView.get(view.index)
        if (!layersOfView.isEmpty) {
            FFContext.notify(RenderEvent)
        } else {
            val layerIterator = layersOfView.iterator()
            while (layerIterator.hasNext()) {
                val layerId = layersOfView.get(layerIterator.next())
                if (!ViewSystem.layers.isActive(layerId))
                    continue
                RenderEvent.layerIndex = layerId
                FFContext.notify(RenderEvent)
            }
        }

        graphics.endRendering(view)
    }



    companion object {
        private var initialized: Boolean = false
        lateinit var eventDispatcher: IEventDispatcher
            private set
        lateinit var graphics: FFGraphics
            private set
        lateinit var audio: FFAudio
            private set
        lateinit var input: FFInput
            private set
        lateinit var timer: FFTimer
            private set
    }

    abstract class SystemTimer {

        abstract val tickAction: () -> Unit

        internal fun tick() {
            tickAction()
        }
    }


    object UpdateEvent : FFEvent<UpdateEvent.Listener>(createTypeKey(UpdateEvent::class.java)) {

        override fun notify(listener: UpdateEvent.Listener) =
            listener()

        interface Listener {
            operator fun invoke()
        }
    }

    object RenderEvent : FFEvent<RenderEvent.Listener>(createTypeKey(RenderEvent::class.java)) {

        internal var viewIndex: Int = -1
        internal var layerIndex: Int = -1
        internal val clip: Rectangle = Rectangle(0, 0, 0, 0)

        override fun notify(listener: RenderEvent.Listener) =
            listener(viewIndex, layerIndex, clip)

        interface Listener {
            operator fun invoke(viewId: Int, layerId: Int, clip: Rectangle)
        }
    }

}