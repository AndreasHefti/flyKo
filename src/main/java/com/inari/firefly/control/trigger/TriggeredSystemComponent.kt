package com.inari.firefly.control.trigger

import com.inari.firefly.Call
import com.inari.firefly.system.component.SystemComponent
import java.util.*

abstract class TriggeredSystemComponent protected constructor(
    objectIndexerName: String
) : SystemComponent(objectIndexerName) {

    private val trigger = BitSet()

    protected fun <A : Trigger> with(cBuilder: Trigger.Subtype<A>, call: Call, configure: (A.() -> Unit)): A {
        val trigger = cBuilder.doBuild(configure)
        TriggerSystem.trigger.receiver()(trigger)
        trigger.register(call)
        return trigger
    }

    override fun dispose() {
        var i = trigger.nextSetBit(0)
        while (i >= 0) {
            TriggerSystem.trigger.delete(i)
            i = trigger.nextSetBit(i + 1)
        }
        super.dispose()
    }
}