package com.inari.firefly.physics.contact

import com.inari.util.event.Event


object ContactEvent : Event<ContactEvent.Listener>(EVENT_ASPECTS.createAspect("ContactEvent")) {

    @JvmField internal var entity: Int = -1

    override fun notify(listener: Listener) {
        listener(entity)
    }

    interface Listener {
        operator fun invoke(entity: Int)
    }
}