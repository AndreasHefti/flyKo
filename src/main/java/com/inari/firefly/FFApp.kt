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

    init {
        if (initialized) {
            throw IllegalStateException("FFApp is a conceptual singleton and is already initialized")
        }

        Companion.eventDispatcher = eventDispatcher()
        Companion.graphics = graphics()
        Companion.audio = audio()
        Companion.input = input()
        Companion.timer = timer()
    }

    fun update() {
        FFApp.timer.tick()
        FFContext.notify(UpdateEvent)
        FFApp.timer.updateSchedulers()
    }

    fun render() {
        val size = ViewSystem.activeViewPorts.size()
        if (size > 0) {
            var i = 0
            while (i < size) {
                val view = ViewSystem.activeViewPorts.get(i++)
                render(view)
            }

            graphics.flush(ViewSystem.activeViewPorts)
        } else {
            render(ViewSystem.baseView)
            graphics.flush(NO_VIRTUAL_VIEW_PORTS)
        }

        FFContext.notify(PostRenderEvent)
    }

    private fun render(view: ViewData) {
        RenderEvent.viewIndex = view.index
        RenderEvent.layerIndex = 0
        RenderEvent.clip.x = Math.floor(view.worldPosition.x.toDouble()).toInt()
        RenderEvent.clip.y = Math.floor(view.worldPosition.y.toDouble()).toInt()
        RenderEvent.clip.width = view.bounds.width
        RenderEvent.clip.height = view.bounds.height

        graphics.startRendering(view, true)

        val layersOfView = ViewSystem.layersOfView.get(view.index)
        if (layersOfView.isEmpty) {
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

        @JvmField internal val NO_VIRTUAL_VIEW_PORTS: DynArrayRO<ViewData> =
            DynArray.create(ViewData::class.java)
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

    object PostRenderEvent : FFEvent<PostRenderEvent.Listener>(createTypeKey(PostRenderEvent::class.java)) {

        override fun notify(listener: PostRenderEvent.Listener) =
            listener()

        interface Listener {
            operator fun invoke()
        }
    }

}