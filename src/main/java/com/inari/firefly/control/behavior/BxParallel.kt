package com.inari.firefly.control.behavior

import com.inari.firefly.OpResult
import com.inari.firefly.system.component.SystemComponentSubType


class BxParallel private constructor() : BxBranch() {

    @JvmField internal var successThreshold: Int = 0

    var ff_SuccessThreshold: Int
        get() = successThreshold
        set(value) { successThreshold = value }

    override fun tick(entityId: Int, behavior: EBehavior): OpResult {
        val threshold = if (successThreshold > childrenRefs.size)
                childrenRefs.size
            else
                successThreshold

        val i = childrenRefs.iterator()
        var successCount = 0
        var failuresCount = 0
        loop@ while (i.hasNext()) {
            when(BehaviorSystem.nodes.map[i.nextInt()]?.tick(entityId, behavior) ?: continue@loop) {
                OpResult.RUNNING -> {}
                OpResult.SUCCESS -> successCount++
                OpResult.FAILED -> failuresCount++
            }
        }
        return if (successCount >= threshold)
            OpResult.SUCCESS
        else if (failuresCount > 0)
            OpResult.FAILED
        else
            OpResult.RUNNING
    }

    companion object : SystemComponentSubType<BxNode, BxParallel>(BxNode, BxParallel::class.java) {
        override fun createEmpty() = BxParallel()
    }

}