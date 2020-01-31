package com.inari.firefly.control.behavior

import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.OpResult
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityActivationEvent
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.java.types.BitSet
import com.inari.util.aspect.Aspect
import com.inari.util.aspect.Aspects
import com.inari.util.aspect.IndexedAspectType
import com.inari.util.collection.BitSetIterator

typealias BxOp = (Int, EBehavior) -> OpResult
typealias BxConditionOp = (Int, EBehavior) -> Boolean

object BehaviorSystem : ComponentSystem  {

    @JvmField val BEHAVIOR_STATE_ASPECT_GROUP = IndexedAspectType("BEHAVIOR_STATE_ASPECT_GROUP")
    @JvmField val UNDEFINED_BEHAVIOR_STATE: Aspect = BEHAVIOR_STATE_ASPECT_GROUP.createAspect("UNDEFINED_BEHAVIOR_STATE")

    @JvmField val TRUE_CONDITION: BxConditionOp = { _, _ -> true }
    @JvmField val FALSE_CONDITION: BxConditionOp = { _, _ -> false }
    @JvmField val NOT: (BxConditionOp) -> BxConditionOp = {
        c -> { entityId, bx -> ! c(entityId, bx) }
    }
    @JvmField val AND: (BxConditionOp, BxConditionOp) -> BxConditionOp = {
        c1, c2 -> { entityId, bx -> c1(entityId, bx) && c2(entityId, bx) }
    }
    @JvmField val OR: (BxConditionOp, BxConditionOp) -> BxConditionOp = {
        c1, c2 -> { entityId, bx -> c1(entityId, bx) || c2(entityId, bx) }
    }
    @JvmField val ACTION_DONE_CONDITION =  { aspect: Aspect -> {
        _ : Int, behavior: EBehavior -> aspect in behavior.actionsDone }
    }
    @JvmField val SUCCESS_ACTION: BxOp = { _, _ -> OpResult.SUCCESS }
    @JvmField val FAIL_ACTION: BxOp = { _, _ -> OpResult.FAILED }


    override val supportedComponents: Aspects =
            SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(BxNode)

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
        behavior.treeState = OpResult.SUCCESS
        behavior.actionsDone.clear()
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

        if (behavior.treeState === OpResult.SUCCESS)
            if (!behavior.repeat)
                return
            else
                reset(entityId)

        behavior.treeState = nodes[behavior.treeRef].tick(entityId, behavior)
    }

    override fun clearSystem() {
        nodes.clear()
    }
}