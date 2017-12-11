package com.inari.firefly.system

import com.inari.commons.event.AspectedEvent
import com.inari.commons.event.AspectedEventListener

abstract class FFAspectedEvent <L: AspectedEventListener>(typeKey: EventTypeKey) : AspectedEvent<L>(typeKey) {
    val typeKey: EventTypeKey = typeKey
}