package com.inari.firefly.physics.movement

import com.inari.util.BitSet
import com.inari.util.event.Event

object MoveEvent : Event<MoveEvent.Listener>(EVENT_ASPECTS.createAspect("MoveEvent")) {

    @JvmField internal val entities: BitSet = BitSet(100)

    override fun notify(listener: Listener) {
        listener(entities)
    }

    interface Listener {
        operator fun invoke(entities: BitSet)
    }
}