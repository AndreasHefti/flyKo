package com.inari.firefly.system

import com.inari.commons.event.Event
import com.inari.commons.event.EventDispatcher
import com.inari.commons.event.IEventDispatcher
import com.inari.commons.event.Event.EventTypeKey





object FFContext {

    private val eventDispatcher: IEventDispatcher = EventDispatcher()

    fun <L> registerListener(eventType: EventTypeKey, listener: L): FFContext {
        eventDispatcher.register(eventType, listener)
        return this
    }

    fun <L> disposeListener(eventType: EventTypeKey, listener: L): FFContext {
        eventDispatcher.unregister(eventType, listener)
        return this
    }

    fun <L> notify(event: Event<L>): FFContext {
        eventDispatcher.notify(event)
        return this
    }
}