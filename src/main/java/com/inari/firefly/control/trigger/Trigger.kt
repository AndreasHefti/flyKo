package com.inari.firefly.control.trigger

import com.inari.firefly.Call
import com.inari.firefly.Condition
import com.inari.firefly.NULL_CALL
import com.inari.firefly.NULL_CONDITION
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent

class Trigger : SystemComponent() {

    @JvmField internal var removeAfter = false
    @JvmField internal var condition: Condition = NULL_CONDITION
    @JvmField internal var call: Call = NULL_CALL

    var ff_RemoveAfter: Boolean
        get() = removeAfter
        set(value) { removeAfter = value }
    var ff_Condition: Condition
        get() = throw UnsupportedOperationException()
        set(value) { condition = value }
    var ff_Call: Call
        get() = throw UnsupportedOperationException()
        set(value) { call = value }

    override fun indexedTypeKey() = typeKey
    companion object : SingleType<Trigger>() {
        override val typeKey = SystemComponent.createTypeKey(Trigger::class.java)
        override fun createEmpty() = Trigger()
    }
}