package com.inari.firefly.control.behavior

import com.inari.firefly.OpResult
import com.inari.firefly.entity.Entity
import com.inari.firefly.system.component.SystemComponentSubType


class BxParallel private constructor() : BxBranch() {

    @JvmField internal var successThreshold: Int = 0

    var ff_SuccessThreshold: Int
        get() = successThreshold
        set(value) { successThreshold = value }

    override fun tick(entity: Entity, behavior: EBehavior): OpResult {
        val threshold = if (successThreshold > children.size)
                children.size
            else
                successThreshold

        var successCount = 0
        var failuresCount = 0
        var i = 0
        loop@ while (i < children.capacity) {
            when(children[i++]?.tick(entity, behavior) ?: continue@loop) {
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