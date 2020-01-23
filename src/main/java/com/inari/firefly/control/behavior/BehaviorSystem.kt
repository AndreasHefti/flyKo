package com.inari.firefly.control.behavior

import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.control.task.TaskSystem
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityActivationEvent
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.aspect.Aspects
import com.inari.util.collection.BitSetIterator
import com.inari.firefly.control.behavior.BehaviorTree.Status
import java.util.*

object BehaviorSystem : ComponentSystem  {

    override val supportedComponents: Aspects =
            SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(BehaviorTree, BxNode)

    @JvmField val trees = ComponentSystem.createComponentMapping(
            BehaviorTree
    )

    @JvmField val nodes = ComponentSystem.createComponentMapping(
            BxNode
    )

    private val entityIds = BitSet()

    init {
        FFContext.registerListener(
                FFApp.UpdateEvent,
                object : FFApp.UpdateEvent.Listener {
                    override fun invoke() = update()
                }
        )

        FFContext.registerListener(
                EntityActivationEvent,
                object : EntityActivationEvent.Listener {
                    override fun entityActivated(entity: Entity) =
                            entityIds.set(entity.index)
                    override fun entityDeactivated(entity: Entity) =
                            entityIds.clear(entity.index)
                    override fun match(aspects: Aspects): Boolean =
                            aspects.contains(EBehavior)
                }
        )

        FFContext.loadSystem(this)
    }

    fun reset(entityId: Int) {
        val behavior = EntitySystem[entityId][EBehavior]
        var i = behavior.runningTasks.nextSetBit(0)
        while (i >= 0) {
            val bTask = nodes[i] as BxTask
            if (bTask.resetEntityTaskRef >= 0)
                TaskSystem.runEntityTask(bTask.resetEntityTaskRef, entityId)
            i = behavior.runningTasks.nextSetBit(i + 1)
        }

        behavior.runningTasks.clear()
        behavior.status = BehaviorTree.Status.RESET
    }

    internal fun update() {
        val iterator = BitSetIterator(entityIds)
        while (iterator.hasNext())
            tick(iterator.next())
    }

    internal fun tick(entityId: Int) {
        val behavior = EntitySystem[entityId][EBehavior]
        if (!behavior.active || behavior.treeRef < 0)
            return

        if (behavior.status == Status.SUCCESS || behavior.status == Status.FAILURE)
            if (!behavior.repeat)
                return
            else
                reset(entityId)

        behavior.status = trees[behavior.treeRef].tick(entityId, behavior)
    }

    override fun clearSystem() {
        trees.clear()
        nodes.clear()
    }
}