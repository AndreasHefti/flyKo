package com.inari.firefly.control.trigger

import com.inari.firefly.*
import com.inari.firefly.component.Component
import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentType
import com.inari.util.BooleanSupplier
import com.inari.util.Call
import com.inari.util.aspect.AspectType

@ComponentDSL
abstract class Trigger protected constructor() : SystemComponent(Trigger::class.java.name) {

    var disposeAfter: Boolean = false
    var condition: BooleanSupplier  = TRUE_SUPPLIER

    abstract fun register(call: Call)

    protected fun doTrigger(call: Call) {
        if (condition()) {
            call()
            if (disposeAfter)
                dispose()
        }
    }

    override fun componentType(): ComponentType<Trigger> = Companion
    companion object : SystemComponentType<Trigger>(Trigger::class.java)

    abstract class Subtype<A : Trigger> : ComponentType<A> {
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
        override val typeClass: Class<out Component> = Trigger.typeClass
        final override val aspectIndex: Int = Trigger.aspectIndex
        final override val aspectName: String = Trigger.aspectName
        final override val aspectType: AspectType = Trigger.aspectType
        protected abstract fun createEmpty(): A
    }
}