package com.inari.firefly.control.trigger

import com.inari.firefly.Call
import com.inari.firefly.Condition
import com.inari.firefly.TRUE_CONDITION
import com.inari.firefly.component.Component
import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentType
import com.inari.util.aspect.AspectType
import com.inari.util.indexed.Indexer

@ComponentDSL
abstract class Trigger protected constructor() : SystemComponent() {

    override val indexer: Indexer =
        Indexer(Trigger::class.java.name)

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

    override fun componentType(): ComponentType<Trigger> =
        Trigger.Companion

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