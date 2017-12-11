package com.inari.firefly.control.task

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.system.TriggerMap
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent.Companion.ASPECT_GROUP

object TaskSystem : ComponentSystem {

    override val supportedComponents: IAspects =
        ASPECT_GROUP.createAspects(Task)

    @JvmField internal val triggerMap = TriggerMap()
    @JvmField val tasks = ComponentSystem.createComponentMapping(
        Task, nameMapping = true
    )

    init {
        FFContext.loadSystem(this)
    }

    fun runTask(name: String) =
        runTask(tasks.indexForName(name))

    fun runTask(taskId: CompId) =
        runTask(taskId.checkType(Task::class.java).index)

    fun runTask(taskIndex: Int) {
        if (taskIndex in tasks) {
            tasks[taskIndex].task()
            if (tasks[taskIndex].removeAfterRun)
                tasks.delete(taskIndex)
        }
    }

    override fun clearSystem() {
        tasks.clear()
    }
}