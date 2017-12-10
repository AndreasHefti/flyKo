package com.inari.firefly.control.task

import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.Call
import com.inari.firefly.NULL_CALL
import com.inari.firefly.system.component.SubType

class FunctionalTask : Task() {

    protected var taskExpr: Call = NULL_CALL

    var ff_TaskExpression: Call
        get() = throw UnsupportedOperationException()
        set(value) {taskExpr = setIfNotInitialized(value, "ff_TaskExpression")}

    override fun run() = taskExpr()

    companion object : SubType<FunctionalTask, Task>() {
        override val typeKey: IndexedTypeKey = Task.typeKey
        override fun subType() = FunctionalTask::class.java
        override fun createEmpty() = FunctionalTask()
    }
}