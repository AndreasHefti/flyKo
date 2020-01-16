package com.inari.firefly.control.behavior

import com.inari.firefly.OpResult
import com.inari.firefly.system.component.SystemComponentSubType

class Condition private constructor() : BehaviorNode() {

    @JvmField internal var condition: (Int, EBehavior) -> Boolean = {
        _, _ -> false
    }

    var ff_Condition: (Int, EBehavior) -> Boolean
        get() = condition
        set(value) { condition = value}

    override fun tick(entityId: Int, behaviour: EBehavior): OpResult =
            when (condition(entityId, behaviour)) {
                true -> OpResult.SUCCESS
                false -> OpResult.FAILED
            }

    companion object : SystemComponentSubType<BehaviorNode, Condition>(BehaviorNode, Condition::class.java) {
        override fun createEmpty() = Condition()
    }

}