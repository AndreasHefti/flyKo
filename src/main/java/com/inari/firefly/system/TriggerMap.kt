package com.inari.firefly.system

import com.inari.commons.lang.list.DynArray
import com.inari.firefly.Call
import com.inari.firefly.Condition
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.external.FFTimer

class TriggerMap(updateResolution: Float = 50f) {

    private val scheduler: FFTimer.UpdateScheduler =
        FFContext.timer.createUpdateScheduler(updateResolution)
    private val trigger: DynArray<Trigger> =
        DynArray.create(Trigger::class.java)

    init {
        FFContext.registerListener(FFApp.UpdateEvent, object : FFApp.UpdateEvent.Listener {
            override fun invoke() {
                if (scheduler.needsUpdate()) {
                    var i = 0
                    while (i < trigger.capacity()) {
                        val t = trigger[i++] ?: continue
                        if (t.condition())
                            t.call()
                    }
                }
            }
        } )
    }

    fun createTrigger(condition: Condition, call : Call): Int {
        val t = Trigger(condition, call)
        trigger.set(t.index(), t)
        return t.index()
    }

    fun disposeTrigger(triggerId: Int) =
        trigger.remove(triggerId)?.dispose()

    interface TriggerCall {
        operator fun invoke(compIndex: Int)
    }
}