package com.inari.firefly.control.behavior

import com.inari.firefly.OpResult
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.system.component.SystemComponentBuilder
import com.inari.firefly.system.component.SystemComponentSubType

class BxDecorator private constructor() : BxNode() {

    @JvmField internal var decoration: (OpResult) -> OpResult = { r -> r }
    @JvmField internal var childRef = -1

    var ff_ChildNode = ComponentRefResolver(BxNode) {
        index -> childRef = index
    }

    fun <C : BxNode> ff_WithChild(cBuilder: SystemComponentBuilder<C>, configure: (C.() -> Unit)): CompId {
        val id = cBuilder.build(configure)
        childRef = id.instanceId
        return id
    }

    override fun tick(entityId: Int, behaviour: EBehavior): OpResult =
            decoration(BehaviorSystem.nodes.map[childRef]?.tick(entityId, behaviour) ?: OpResult.FAILED)

    companion object : SystemComponentSubType<BxNode, BxDecorator>(BxNode, BxDecorator::class.java) {
        override fun createEmpty() = BxDecorator()
    }

}