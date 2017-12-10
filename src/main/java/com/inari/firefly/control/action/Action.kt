package com.inari.firefly.control.action

import com.inari.firefly.Condition
import com.inari.firefly.Expr
import com.inari.firefly.NULL_EXPR
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent
import java.util.*

class Action private constructor() : SystemComponent() {

    @JvmField internal var entityAction: Expr<Entity> = NULL_EXPR()

    private val triggerIds = BitSet()

    var ff_Action: Expr<Entity>
        get() = throw UnsupportedOperationException()
        set(value) {entityAction = setIfNotInitialized(value, "ff_Action")}

    fun ff_createTrigger(entityId: Int, condition: Condition): Int {
        val tId = ActionSystem.triggerMap.createTrigger(condition, {
            entityAction(EntitySystem[entityId])
        })
        triggerIds.set(tId)
        return tId
    }

    fun ff_disposeTrigger(triggerId: Int) {
        if (triggerIds[triggerId]) {
            ActionSystem.triggerMap.disposeTrigger(triggerId)
            triggerIds.set(triggerId, false)
        }
    }

    override fun dispose() {
        if (!triggerIds.isEmpty) {
            var i = triggerIds.nextSetBit(0)
            while (i >= 0) {
                ActionSystem.triggerMap.disposeTrigger(i)
                i = triggerIds.nextSetBit(i + 1)
            }
            triggerIds.clear()
        }
        super.dispose()
    }

    override fun indexedTypeKey() = typeKey
    companion object : SingleType<Action>() {
        override val typeKey = SystemComponent.createTypeKey(Action::class.java)
        override fun createEmpty() = Action()
    }
}