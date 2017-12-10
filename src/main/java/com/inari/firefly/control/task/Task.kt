package com.inari.firefly.control.task

import com.inari.firefly.Call
import com.inari.firefly.Condition
import com.inari.firefly.NULL_CALL
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent

class Task private constructor() : SystemComponent() {

    @JvmField internal var removeAfterRun = false
    @JvmField internal var task: Call = NULL_CALL

    private var triggerId = -1

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
                TaskSystem.triggerMap.disposeTrigger(triggerId)
            triggerId = TaskSystem.triggerMap.createTrigger(value, { task() })
        }

    override fun dispose() {
        if (triggerId >= 0)
            TaskSystem.triggerMap.disposeTrigger(triggerId)
        super.dispose()
    }

    override fun indexedTypeKey() = typeKey
    companion object : SingleType<Task>() {
        override val typeKey = SystemComponent.createTypeKey(Task::class.java)
        override fun createEmpty() = Task()
    }
}