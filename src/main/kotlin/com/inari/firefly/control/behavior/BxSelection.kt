package com.inari.firefly.control.behavior

import com.inari.firefly.OpResult
import com.inari.firefly.entity.Entity
import com.inari.firefly.system.component.SystemComponentSubType

class BxSelection private constructor() : BxBranch() {

    override fun tick(entity: Entity, behavior: EBehavior): OpResult {
        var i = 0
        loop@ while (i < children.capacity) {
            when(children[i++]?.tick(entity, behavior) ?: continue@loop) {
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