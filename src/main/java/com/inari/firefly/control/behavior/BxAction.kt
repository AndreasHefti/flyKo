package com.inari.firefly.control.behavior

import com.inari.firefly.OpResult
import com.inari.firefly.control.behavior.BehaviorSystem.SUCCESS_ACTION
import com.inari.firefly.control.behavior.BehaviorSystem.UNDEFINED_BEHAVIOR_STATE
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.aspect.Aspect

class BxAction private constructor() : BxNode() {

    @JvmField internal var tickOp: BxOp = SUCCESS_ACTION
    @JvmField internal var state: Aspect = UNDEFINED_BEHAVIOR_STATE

    var ff_TickOp: BxOp
        get() = throw UnsupportedOperationException()
        set(value) { tickOp = value }

    var ff_State: Aspect
        get() = state
        set(value) =
            if (BehaviorSystem.BEHAVIOR_STATE_ASPECT_GROUP.typeCheck(value)) state = value
            else throw IllegalArgumentException()

    override fun tick(entityId: Int, behavior: EBehavior): OpResult {
        val result = tickOp(entityId, behavior)
        if (result === OpResult.SUCCESS)
            behavior.actionsDone + state
        return result
    }

    companion object : SystemComponentSubType<BxNode, BxAction>(BxNode, BxAction::class.java) {
        override fun createEmpty() = BxAction()
    }

}