package com.inari.firefly.control.task

import com.inari.firefly.Call
import com.inari.firefly.NULL_CALL
import com.inari.firefly.component.ComponentType
import com.inari.firefly.control.trigger.Trigger
import com.inari.firefly.control.trigger.TriggeredSystemComponent
import com.inari.firefly.system.component.SingleType

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

    fun <A : Trigger> withTrigger(cBuilder: Trigger.Subtype<A>, configure: (A.() -> Unit)): A =
        super.with(cBuilder, triggerCall, configure)

    override fun componentType(): ComponentType<Task> =
        Task.Companion

    companion object : SingleType<Task>() {
        override val indexedTypeKey by lazy { TypeKeyBuilder.create(Task::class.java) }
        override fun createEmpty() = Task()
    }
}