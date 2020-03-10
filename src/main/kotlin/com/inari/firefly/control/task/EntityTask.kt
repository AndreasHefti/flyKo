package com.inari.firefly.control.task

import com.inari.firefly.EMPTY_INT_OPERATION
import com.inari.firefly.component.CompId
import com.inari.firefly.control.trigger.Trigger
import com.inari.firefly.control.trigger.TriggeredSystemComponent
import com.inari.firefly.system.component.SystemComponentSingleType
import com.inari.util.IntOperation

class EntityTask private constructor() : TriggeredSystemComponent(EntityTask::class.java.name) {

    @JvmField internal var entityTask: IntOperation = EMPTY_INT_OPERATION

    /** The task to run as IntOperation
     *  <p>
     *  An IntOperation is defined within an interface so one can be added as an already existing instance that
     *  implements this interface or with an anonymous class implementation in the form:
     *  ff_Task = object : IntOperation { ... }
     */
    var ff_Task: IntOperation
        get() = throw UnsupportedOperationException()
        set(value) { entityTask = setIfNotInitialized(value, "ff_Task") }

    fun <A : Trigger> ff_WithTrigger(cBuilder: Trigger.Subtype<A>, entityId: CompId, configure: (A.() -> Unit)): A =
            super.ff_With(cBuilder, { entityTask(entityId.instanceId)}, configure)

    override fun componentType() = Companion
    companion object : SystemComponentSingleType<EntityTask>(EntityTask::class.java) {
        override fun createEmpty() = EntityTask()
    }
}