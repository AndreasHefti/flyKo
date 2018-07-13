package com.inari.firefly.graphics.view

import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.util.event.Event

object ViewChangeEvent : Event<ViewChangeEvent.Listener>(EVENT_ASPECTS.createAspect("ViewChangeEvent")) {

    enum class Type {
        POSITION,
        ORIENTATION,
        SIZE
    }

    private lateinit var id: CompId
    private lateinit var type: ViewChangeEvent.Type

    override fun notify(listener: ViewChangeEvent.Listener) =
        listener(id, type)

    fun send(id: CompId, type: ViewChangeEvent.Type) {
        this.id = id
        this.type = type
        FFContext.notify(this)
    }

    interface Listener {
        operator fun invoke(id: CompId, type: Type)
    }
}