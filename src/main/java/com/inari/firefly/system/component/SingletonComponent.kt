package com.inari.firefly.system.component

import com.inari.firefly.FFContext
import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentType

abstract class SingletonComponent<CC : C, C : SystemComponent> : ComponentType<C> {
    abstract fun subType(): Class<CC>
    protected abstract fun create(): CC

    val id: CompId
        get() {
            return if (FFContext.mapper(this).contains(subType().simpleName))
                 FFContext[this, subType().simpleName].componentId
            else NO_COMP_ID
        }

    val instance: CC
        get() {
            if (!FFContext.mapper(this).contains(subType().simpleName)) {
                val comp = create()
                comp.ff_Name = subType().simpleName
                comp.name()
                FFContext.mapper(this).receiver()(comp)
            }

            @Suppress("UNCHECKED_CAST")
            return FFContext[this, subType().simpleName] as CC
        }

    fun dispose() {
        if (FFContext.mapper(this).contains(subType().simpleName))
            FFContext.delete(this, subType().simpleName)
    }
}