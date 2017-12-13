package com.inari.firefly.system.component

import com.inari.firefly.FFContext
import com.inari.firefly.component.ComponentType

abstract class SingletonComponent<CC : C, C : SystemComponent> : ComponentType<C> {
    abstract fun subType(): Class<CC>
    protected abstract fun create(): CC

    fun get(): CC {
        if (!FFContext.mapper(this).contains(subType().simpleName)) {
            val comp = create()
            comp.ff_Name = subType().simpleName
            comp.name()
            FFContext.mapper(this).receiver()(comp)
        }

        @Suppress("UNCHECKED_CAST")
        return FFContext[this, subType().simpleName] as CC
    }

    fun activate(): CC {
        val singleton = get()
        FFContext.activate(singleton.componentId)
        return singleton
    }
}