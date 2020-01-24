package com.inari.firefly.control.behavior

import com.inari.firefly.OpResult
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.task.EntityTask
import com.inari.firefly.control.task.TaskSystem
import com.inari.firefly.system.component.SystemComponentSubType

class BxTask private constructor() : BxNode() {

    @JvmField internal var entityTaskRef: Int = -1
    @JvmField internal var resetEntityTaskRef: Int = -1

    val ff_Task = ComponentRefResolver(EntityTask) { index-> entityTaskRef = index }
    val ff_ResetTask = ComponentRefResolver(EntityTask) { index-> resetEntityTaskRef = index }

    override fun tick(entityId: Int, behaviour: EBehavior): OpResult =
            TaskSystem.runEntityTask(entityTaskRef, entityId)

    companion object : SystemComponentSubType<BxNode, BxTask>(BxNode, BxTask::class.java) {
        override fun createEmpty() = BxTask()
    }

}