package com.inari.firefly.control.task

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.Call
import com.inari.firefly.Condition
import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.component.IComponentMap
import com.inari.firefly.system.TriggerMap
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent.Companion.ASPECT_GROUP

object TaskSystem : ComponentSystem {

    override val supportedComponents: IAspects =
        ASPECT_GROUP.createAspects(Task)

    private val triggerMap = TriggerMap({ index -> createRunCall(index) })
    @JvmField val tasks = ComponentSystem.createComponentMapping(
        Task,
        nameMapping = true,
        listener = { task, action -> when (action) {
            IComponentMap.MapAction.DELETED -> triggerMap.disposeTrigger(task.index())
            else -> {}
        } }
    )

    init {
        FFContext.loadSystem(this)
    }

    fun runTask(taskIndex: Int) {
        if (taskIndex in tasks) {
            tasks[taskIndex].run()
            if (tasks[taskIndex].ff_RemoveAfterRun)
                tasks.delete(taskIndex)
        }
    }

    fun createTrigger(taskName: String, condition: Condition) =
        createTrigger(tasks.indexForName(taskName), condition)

    fun createTrigger(taskId: CompId, condition: Condition) =
        createTrigger(taskId.index, condition)

    fun createTrigger(taskIndex: Int, condition: Condition) {
        if (taskIndex in tasks)
            triggerMap.createTrigger(taskIndex, condition)
    }

    fun createRunCall(taskIndex: Int): Call = {
        runTask(taskIndex)
    }

    override fun clearSystem() {
        tasks.clear()
    }
}