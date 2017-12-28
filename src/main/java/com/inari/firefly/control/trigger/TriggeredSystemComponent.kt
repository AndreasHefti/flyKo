package com.inari.firefly.control.trigger

import com.inari.firefly.Call
import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.component.CompId
import com.inari.firefly.system.component.SystemComponent
import java.util.*

abstract class TriggeredSystemComponent : SystemComponent() {

    private val trigger = BitSet()
    protected abstract fun triggerCall(compId: CompId = NO_COMP_ID): Call

    fun <A : Trigger> with(cBuilder: Trigger.Subtype<A>, configure: (A.() -> Unit)): A {
        val trigger = cBuilder.doBuild(configure)
        Trigger.TRIGGER_MAP[trigger.index()] = trigger
        trigger.register(triggerCall())
        return trigger
    }

    fun <A : Trigger> with(cBuilder: Trigger.Subtype<A>, compId: CompId, configure: (A.() -> Unit)): A {
        val trigger = cBuilder.doBuild(configure)
        Trigger.TRIGGER_MAP[trigger.index()] = trigger
        trigger.register(triggerCall(compId))
        return trigger
    }

    override fun dispose() {
        var i = trigger.nextSetBit(0)
        while (i >= 0) {
            val t = Trigger.TRIGGER_MAP[i]
            t.dispose()
            i = trigger.nextSetBit(i + 1)
        }
        super.dispose()
    }
}