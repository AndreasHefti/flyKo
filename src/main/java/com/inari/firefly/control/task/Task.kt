package com.inari.firefly.control.task

import com.inari.firefly.Call
import com.inari.firefly.Condition
import com.inari.firefly.NULL_CALL
import com.inari.firefly.control.trigger.Trigger
import com.inari.firefly.control.trigger.TriggerSystem
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent

class Task private constructor() : SystemComponent() {

    @JvmField internal var removeAfterRun = false
    @JvmField internal var task: Call = NULL_CALL

    private var triggerId = -1
    private val triggerCall = { task() }

    var ff_RemoveAfterRun: Boolean
        get() = removeAfterRun
        set(value) { removeAfterRun = value }
    var ff_Task: Call
        get() = throw UnsupportedOperationException()
        set(value) { task = value }
    var ff_Trigger: Condition
        get() = throw UnsupportedOperationException()
        set(value) {
            if (triggerId >= 0)
                TriggerSystem.triggers.delete(triggerId)

            val call = triggerCall
            triggerId = Trigger.build {
                ff_Condition = value
                ff_Call = call
            }.index
        }

    override fun dispose() {
        if (triggerId >= 0)
            TriggerSystem.triggers.delete(triggerId)
        super.dispose()
    }

    override fun indexedTypeKey() = typeKey
    companion object : SingleType<Task>() {
        override val typeKey = SystemComponent.createTypeKey(Task::class.java)
        override fun createEmpty() = Task()
    }
}