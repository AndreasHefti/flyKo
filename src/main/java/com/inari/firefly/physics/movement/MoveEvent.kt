package com.inari.firefly.physics.movement

import com.inari.firefly.system.FFEvent
import java.util.*

object MoveEvent : FFEvent<MoveEvent.Listener>(createTypeKey(MoveEvent::class.java)) {

    @JvmField internal val entities: BitSet = BitSet(100)

    override fun notify(listener: Listener) {
        listener(entities)
    }

    interface Listener {
        operator fun invoke(entities: BitSet)
    }
}