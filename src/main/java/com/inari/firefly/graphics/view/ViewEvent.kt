package com.inari.firefly.graphics.view

import com.inari.firefly.component.CompId
import com.inari.firefly.FFContext
import com.inari.firefly.system.FFEvent
import com.inari.firefly.external.ViewPortData


typealias ViewEventListener = (CompId, ViewPortData, ViewEvent.Type) -> Unit
object ViewEvent : FFEvent<ViewEventListener>(createTypeKey(ViewEvent::class.java)) {

    enum class Type {
        VIEW_CREATED,
        VIEW_ACTIVATED,
        VIEW_DISPOSED,
        VIEW_DELETED
    }

    private lateinit var id: CompId
    private lateinit var data: ViewPortData
    private lateinit var type: ViewEvent.Type

    override fun notify(listener: ViewEventListener) =
        listener(id, data, type)

    fun send(id: CompId, data: ViewPortData, type: ViewEvent.Type) {
        this.id = id
        this.data = data
        this.type = type
        FFContext.notify(this)
    }
}