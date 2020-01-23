package com.inari.firefly.control.behavior

import com.inari.firefly.OpResult
import com.inari.firefly.system.component.SystemComponentSubType

class BxSequence private constructor() : BxBranch() {

    override fun tick(entityId: Int, behaviour: EBehavior): OpResult {
        val i = childrenRefs.iterator()
        loop@ while (i.hasNext()) {
            when(BehaviorSystem.nodes.map[i.nextInt()]?.tick(entityId, behaviour) ?: continue@loop) {
                OpResult.RUNNING -> return OpResult.RUNNING
                OpResult.FAILED -> return OpResult.FAILED
                OpResult.SUCCESS -> {}
            }
        }
        return OpResult.SUCCESS
    }

    companion object : SystemComponentSubType<BxNode, BxSequence>(BxNode, BxSequence::class.java) {
        override fun createEmpty() = BxSequence()
    }
}