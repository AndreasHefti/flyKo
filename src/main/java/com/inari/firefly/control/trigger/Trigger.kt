package com.inari.firefly.control.trigger

import com.inari.commons.lang.indexed.BaseIndexedObject
import com.inari.commons.lang.list.DynArray
import com.inari.firefly.Call
import com.inari.firefly.Condition
import com.inari.firefly.TRUE_CONDITION
import com.inari.firefly.component.ComponentDSL

@ComponentDSL
abstract class Trigger protected constructor() : BaseIndexedObject() {

    @JvmField protected var disposeAfter = false
    @JvmField protected var condition: Condition = TRUE_CONDITION

    var ff_DisposeAfter: Boolean
        get() = disposeAfter
        set(value) { disposeAfter = value }
    var ff_Condition: Condition
        get() = throw UnsupportedOperationException()
        set(value) { condition = value }

    abstract fun register(call: Call)

    protected fun doTrigger(call: Call) {
        if (condition()) {
            call()
            if (disposeAfter)
                dispose()
        }
    }

    override fun dispose() {
        Trigger.TRIGGER_MAP.remove(index)
        super.dispose()
    }

    override fun indexedObjectType() = Trigger::class.java
    companion object {
        @JvmField internal val TRIGGER_MAP = DynArray.create(Trigger::class.java)
    }

    abstract class Subtype<out A : Trigger> {
        fun doBuild(configure: A.() -> Unit): A {
            val result = createEmpty()
            result.also(configure)
            TRIGGER_MAP.set(result.index, result)
            return result
        }
        protected abstract fun createEmpty(): A
    }
}