package com.inari.firefly.control.action

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.Condition
import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.component.IComponentMap
import com.inari.firefly.control.task.TaskSystem
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.system.TriggerMap
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent.Companion.ASPECT_GROUP

object ActionSystem : ComponentSystem {
    override val supportedComponents: IAspects =
        ASPECT_GROUP.createAspects(Action)

    private val triggerMap = TriggerMap()
    @JvmField val actions = ComponentSystem.createComponentMapping(
        Action,
        nameMapping = true,
        listener = { a, action -> when (action) {
            IComponentMap.MapAction.DELETED -> triggerMap.disposeTrigger(a.index())
            else -> {}
        } }
    )

    init {
        FFContext.loadSystem(this)
    }

    fun createTrigger(actionName: String, entityName: String, condition: Condition) =
        createTrigger(
            TaskSystem.tasks.indexForName(actionName),
            EntitySystem.entities.indexForName(entityName),
            condition
        )

    fun createTrigger(actionId: CompId, entityId: CompId, condition: Condition) =
        createTrigger(actionId.index, entityId.index, condition)

    fun createTrigger(actionIndex: Int, entityIndex: Int, condition: Condition) {
        if (actionIndex in actions)
            triggerMap.createTrigger(
                { actions[actionIndex].entityAction(EntitySystem.entities[entityIndex]) },
                condition
            )
    }

    override fun clearSystem() {
        actions.clear()
    }

}