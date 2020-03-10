package com.inari.firefly.control.task

import com.inari.firefly.NULL_CALL
import com.inari.firefly.component.ComponentType
import com.inari.firefly.control.trigger.Trigger
import com.inari.firefly.control.trigger.TriggeredSystemComponent
import com.inari.firefly.system.component.SystemComponentSingleType
import com.inari.util.Call

class SystemTask private constructor() : TriggeredSystemComponent(SystemTask::class.java.name) {

    @JvmField internal var removeAfterRun = false
    @JvmField internal var task: Call = NULL_CALL

    private val triggerCall = { task() }

    var ff_RemoveAfterRun: Boolean
        get() = removeAfterRun
        set(value) { removeAfterRun = value }
    var ff_Task: Call
        get() = throw UnsupportedOperationException()
        set(value) { task = value }

    fun <A : Trigger> ff_WithTrigger(cBuilder: Trigger.Subtype<A>, configure: (A.() -> Unit)): A =
        super.ff_With(cBuilder, triggerCall, configure)

    override fun componentType(): ComponentType<SystemTask> = Companion
    companion object : SystemComponentSingleType<SystemTask>(SystemTask::class.java) {
        override fun createEmpty() = SystemTask()
    }
}