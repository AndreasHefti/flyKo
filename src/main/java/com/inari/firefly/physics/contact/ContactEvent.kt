package com.inari.firefly.physics.contact

import com.inari.firefly.system.FFEvent

object ContactEvent : FFEvent<ContactEvent.Listener>(createTypeKey(ContactEvent::class.java)) {

    @JvmField internal var entity: Int = -1

    override fun notify(listener: ContactEvent.Listener) {
        listener(entity)
    }

    interface Listener {
        operator fun invoke(entity: Int)
    }
}