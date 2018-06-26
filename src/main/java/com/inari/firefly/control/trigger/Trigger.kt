package com.inari.firefly.control.trigger

import com.inari.firefly.Call
import com.inari.firefly.Condition
import com.inari.firefly.TRUE_CONDITION
import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent

@ComponentDSL
abstract class Trigger protected constructor() : SystemComponent() {

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

    final override fun indexedTypeKey() = typeKey
    companion object : ComponentType<Trigger> {
        override val typeKey = SystemComponent.createTypeKey(Trigger::class.java)
    }

    abstract class Subtype<out A : Trigger> {
        internal fun doBuild(configure: A.() -> Unit): A {
            val result = createEmpty()
            result.also(configure)
            TriggerSystem.trigger.receiver()(result)
            return result
        }
        fun build(call: Call, configure: A.() -> Unit): Int {
            val result = doBuild(configure)
            result.register(call)
            return result.index
        }
        protected abstract fun createEmpty(): A
    }
}