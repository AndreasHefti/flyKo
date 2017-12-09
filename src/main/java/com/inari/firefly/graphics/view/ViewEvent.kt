package com.inari.firefly.graphics.view

import com.inari.firefly.component.CompId
import com.inari.firefly.FFContext
import com.inari.firefly.system.FFEvent
import com.inari.firefly.external.ViewPortData

object ViewEvent : FFEvent<ViewEvent.Listener>(createTypeKey(ViewEvent::class.java)) {

    enum class Type {
        VIEW_CREATED,
        VIEW_ACTIVATED,
        VIEW_DISPOSED,
        VIEW_DELETED
    }

    private lateinit var id: CompId
    private lateinit var data: ViewPortData
    private lateinit var type: ViewEvent.Type

    override fun notify(listener: ViewEvent.Listener) =
        listener(id, data, type)

    fun send(id: CompId, data: ViewPortData, type: ViewEvent.Type) {
        this.id = id
        this.data = data
        this.type = type
        FFContext.notify(this)
    }

    interface Listener {
        operator fun invoke(id: CompId, viewPort: ViewPortData, type: Type)
    }
}