package com.inari.firefly.misc

import com.inari.firefly.component.CompId
import com.inari.firefly.FFContext
import com.inari.util.event.Event

typealias TestEventListener = (CompId) -> Unit
object StaticTestEvent : Event<TestEventListener>(EVENT_ASPECTS.createAspect("TestEventListener")) {

    var id: CompId? = null

    override fun notify(listener: TestEventListener) =
        listener(id!!)

    inline fun send(init: StaticTestEvent.() -> Unit) {
        this.also(init)
        FFContext.notify(this)
    }
}