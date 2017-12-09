package com.inari.firefly.system

import com.inari.commons.lang.list.DynArray
import com.inari.firefly.Call
import com.inari.firefly.Condition
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.external.FFTimer

class TriggerMap(
    val callFactory: (Int) -> Call = {throw UnsupportedOperationException("No Call-Factory defined")},
    updateResolution: Float = 50f
) {

    private val scheduler: FFTimer.UpdateScheduler =
        FFContext.timer.createUpdateScheduler(updateResolution)
    private val trigger: DynArray<Trigger> =
        DynArray.create(Trigger::class.java)

    init {
        FFContext.registerListener( FFApp.UpdateEvent, object : FFApp.UpdateEvent.Listener {
            override fun invoke() {
                if (scheduler.needsUpdate()) {
                    var i = 0
                    while (i < trigger.capacity()) {
                        val t = trigger[i] ?: continue
                        if (t.condition())
                            t.call()
                    }
                }
            }
        } )
    }

    fun createTrigger(compIndex: Int, condition: Condition) =
        trigger.add(Trigger(condition, callFactory(compIndex)))

    fun createTrigger(call: Call, condition: Condition) =
        trigger.add(Trigger(condition, call))

    fun disposeTrigger(compIndex: Int) =
        trigger.remove(compIndex)

}