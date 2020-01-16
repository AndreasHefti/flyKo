package com.inari.firefly.control.behavior

import com.inari.firefly.OpResult
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentBuilder
import com.inari.firefly.system.component.SystemComponentSingleType

class BehaviorTree private constructor() : SystemComponent(BehaviorTree::class.java.name) {

    enum class Status {
        RESET,
        RUNNING,
        SUCCESS,
        FAILURE
    }

    @JvmField internal var rootNodeRef: Int = -1

    var ff_RootNode = ComponentRefResolver(BehaviorTree) {
                index -> rootNodeRef = index
            }

    fun <C : BehaviorNode> ff_WithRootNode(cBuilder: SystemComponentBuilder<C>, configure: (C.() -> Unit)): CompId {
        val id = cBuilder.build(configure)
        rootNodeRef = id.instanceId
        return id
    }

    fun tick(entityId: Int, behaviour: EBehavior): Status =
        when (BehaviorSystem.nodes[rootNodeRef].tick(entityId, behaviour)) {
            OpResult.RUNNING -> Status.RUNNING
            OpResult.FAILED -> Status.FAILURE
            OpResult.SUCCESS -> Status.SUCCESS
        }


    override fun componentType() = Companion
    companion object : SystemComponentSingleType<BehaviorTree>(BehaviorTree::class.java) {
        override fun createEmpty() = BehaviorTree()
    }
}