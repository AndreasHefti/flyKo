package com.inari.firefly.control.action

import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.component.CompId
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentSingleType

class Action private constructor() : SystemComponent(Action::class.java.name) {

@JvmField var actionCall: ActionCall = { _, _, _, _ -> }

    var ff_ActionCall: ActionCall
        get() = actionCall
        set(value) { actionCall = value }

    fun call(
            entity1: CompId = NO_COMP_ID,
            entity2: CompId = NO_COMP_ID,
            entity3: CompId = NO_COMP_ID,
            entity4: CompId = NO_COMP_ID) {

        actionCall(entity1, entity2, entity3, entity4)
    }

    override fun componentType() = Companion
    companion object : SystemComponentSingleType<Action>(Action::class.java) {
        override fun createEmpty() = Action()
    }
}


