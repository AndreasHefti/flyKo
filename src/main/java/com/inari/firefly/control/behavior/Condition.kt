package com.inari.firefly.control.behavior

import com.inari.firefly.FALSE_INT_PREDICATE
import com.inari.firefly.IntPredicate
import com.inari.firefly.OpResult
import com.inari.firefly.system.component.SystemComponentSubType

class Condition private constructor() : BehaviorNode() {

    @JvmField internal var condition: IntPredicate = FALSE_INT_PREDICATE

    var ff_Condition: IntPredicate
        get() = condition
        set(value) { condition = value}

    override fun tick(entityId: Int, behaviour: EBehavior): OpResult =
            when (condition(entityId)) {
                true -> OpResult.SUCCESS
                false -> OpResult.FAILED
            }

    companion object : SystemComponentSubType<BehaviorNode, Condition>(BehaviorNode, Condition::class.java) {
        override fun createEmpty() = Condition()
    }

}