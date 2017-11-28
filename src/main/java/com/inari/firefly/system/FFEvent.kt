package com.inari.firefly.system

import com.inari.commons.event.Event

abstract class FFEvent<L>(typeKey: EventTypeKey) : Event<L>(typeKey) {
    val typeKey: EventTypeKey = typeKey
}