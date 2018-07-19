package com.inari.firefly.control.action

import com.inari.firefly.Expr
import com.inari.firefly.NULL_EXPR
import com.inari.firefly.component.CompId
import com.inari.firefly.control.trigger.Trigger
import com.inari.firefly.control.trigger.TriggeredSystemComponent
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.system.component.SystemComponentSingleType
import java.util.*

class Action private constructor() : TriggeredSystemComponent(Action::class.java.name) {

    @JvmField internal var entityAction: Expr<Entity> = NULL_EXPR()

    private val triggerIds = BitSet()

    var ff_Action: Expr<Entity>
        get() = throw UnsupportedOperationException()
        set(value) { entityAction = setIfNotInitialized(value, "ff_Action") }

    fun <A : Trigger> withTrigger(cBuilder: Trigger.Subtype<A>, entityId: CompId, configure: (A.() -> Unit)): A =
        super.with(cBuilder, { entityAction(EntitySystem[entityId])}, configure)

    override fun componentType() =
        Action.Companion

    companion object : SystemComponentSingleType<Action>(Action::class.java) {
        override fun createEmpty() = Action()
    }
}