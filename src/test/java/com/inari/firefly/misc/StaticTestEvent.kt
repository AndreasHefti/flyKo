package com.inari.firefly.misc

import com.inari.commons.event.Event
import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.component.CompId
import com.inari.firefly.system.FFContext

object StaticTestEvent : Event<TestEventListener>(createTypeKey(StaticTestEvent::class.java)) {

    val EVENT_KEY: EventTypeKey = indexedTypeKey

    var id: CompId? = null
    lateinit var key: IndexedTypeKey

    override fun notify(listener: TestEventListener) {
        listener.notifyComponentCreation(id!!, key)
    }

    inline fun send(init: StaticTestEvent.() -> Unit) {
        this.also(init)
        FFContext.notify(this)
    }


}