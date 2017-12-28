package com.inari.firefly.control.task

import com.inari.firefly.Call
import com.inari.firefly.NULL_CALL
import com.inari.firefly.component.CompId
import com.inari.firefly.control.trigger.TriggeredSystemComponent
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent

class Task private constructor() : TriggeredSystemComponent() {


    @JvmField internal var removeAfterRun = false
    @JvmField internal var task: Call = NULL_CALL

    private val triggerCall = { task() }

    var ff_RemoveAfterRun: Boolean
        get() = removeAfterRun
        set(value) { removeAfterRun = value }
    var ff_Task: Call
        get() = throw UnsupportedOperationException()
        set(value) { task = value }

    override fun triggerCall(compId: CompId): Call =
        triggerCall

    override fun indexedTypeKey() = typeKey
    companion object : SingleType<Task>() {
        override val typeKey = SystemComponent.createTypeKey(Task::class.java)
        override fun createEmpty() = Task()
    }
}