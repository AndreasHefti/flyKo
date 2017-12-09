package com.inari.firefly.control.action

import com.inari.firefly.Expr
import com.inari.firefly.entity.Entity
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent

class Action private constructor() : SystemComponent() {

    @JvmField internal var entityAction: Expr<Entity> = {}

    protected val action
        get() = entityAction

    var ff_EntityAction: Expr<Entity>
        get() = throw UnsupportedOperationException()
        set(value) {entityAction = setIfNotInitialized(value, "ff_EntityAction")}

    override fun indexedTypeKey() = typeKey

    companion object : SingleType<Action>() {
        override val typeKey = SystemComponent.createTypeKey(Action::class.java)
        override fun createEmpty() = Action()
    }
}