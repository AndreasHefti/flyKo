package com.inari.firefly.control.behavior

import com.inari.firefly.FALSE_INT_PREDICATE
import com.inari.firefly.IntPredicate
import com.inari.firefly.OpResult
import com.inari.firefly.system.component.SystemComponentSubType

class BxCondition private constructor() : BxNode() {

    @JvmField internal var condition: IntPredicate = FALSE_INT_PREDICATE

    var ff_Condition: IntPredicate
        get() = condition
        set(value) { condition = value}

    override fun tick(entityId: Int, behaviour: EBehavior): OpResult =
            when (condition(entityId)) {
                true -> OpResult.SUCCESS
                false -> OpResult.FAILED
            }

    companion object : SystemComponentSubType<BxNode, BxCondition>(BxNode, BxCondition::class.java) {
        override fun createEmpty() = BxCondition()
    }

}