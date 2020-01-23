package com.inari.firefly.control.behavior

import com.inari.firefly.OpResult
import com.inari.firefly.system.component.SystemComponentSubType

class BxSelection private constructor() : BxBranch() {

    override fun tick(entityId: Int, behaviour: EBehavior): OpResult {
        val i = childrenRefs.iterator()
        loop@ while (i.hasNext()) {
            when(BehaviorSystem.nodes.map[i.nextInt()]?.tick(entityId, behaviour) ?: continue@loop) {
                OpResult.RUNNING -> return OpResult.RUNNING
                OpResult.SUCCESS -> return OpResult.SUCCESS
                OpResult.FAILED -> {}
            }
        }
        return OpResult.FAILED
    }

    companion object : SystemComponentSubType<BxNode, BxSelection>(BxNode, BxSelection::class.java) {
        override fun createEmpty() = BxSelection()
    }

}