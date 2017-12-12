package com.inari.firefly.physics.animation

import com.inari.commons.lang.indexed.BaseIndexedObject
import com.inari.firefly.NO_PROPERTY_REF
import com.inari.firefly.Receiver
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.property.IVirtualPropertyRef

abstract class AnimatedProperty protected constructor() {

    @JvmField internal var animationRef = -1
    @JvmField internal var looping: Boolean = false
    @JvmField internal var propertyRef: IVirtualPropertyRef = NO_PROPERTY_REF
    @JvmField internal var lastUpdate: Long = -1

    @JvmField internal var active = false


    val ff_Animation =
        ComponentRefResolver(Animation, { index->
            animationRef = if (active) throw IllegalStateException() else index
        })

    var ff_Looping: Boolean
        get() = looping
        set(value) { looping = value }

    var ff_PropertyRef: IVirtualPropertyRef
        get() = propertyRef
        set(value) { propertyRef = if (active) throw IllegalStateException() else value }

    fun activate(): AnimatedProperty {
        reset()
        active = true
        return this
    }

    fun deactivate(): AnimatedProperty {
        active = false
        return this
    }

    internal fun compile(entity: Entity) {
        init(entity)
    }

    abstract protected fun init(entity: Entity)
    abstract fun reset()
    
    abstract class Builder<A : AnimatedProperty> {
        private fun doBuild(instance: A, configure: A.() -> Unit, receiver: (A) -> A): A {
            instance.also(configure)
            receiver(instance)
            return instance
        }
        internal fun builder(receiver: Receiver<A>): (A.() -> Unit) -> A = {
            configure -> doBuild(createEmpty(), configure, receiver)
        }
        internal abstract fun createEmpty(): A
    }

}