package com.inari.firefly.system.component

import com.inari.firefly.FFContext
import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.component.CompId

abstract class SingletonComponent<CC : C, C : SystemComponent> : SubType<CC, C>() {

    override fun createEmpty(): CC = throw UnsupportedOperationException()
    protected abstract fun create(): CC

    val id: CompId
        get() {
            return if (FFContext.mapper(this).contains(subType.simpleName))
                 FFContext[this, subType.simpleName].componentId
            else NO_COMP_ID
        }

    val instance: CC
        get() {
            if (!FFContext.mapper(this).contains(subType.simpleName)) {
                val comp = create()
                comp.ff_Name = subType.simpleName
                FFContext.mapper(this).receiver()(comp)
            }

            @Suppress("UNCHECKED_CAST")
            return FFContext[this, subType.simpleName]
        }

    fun dispose() {
        if (FFContext.mapper(this).contains(subType.simpleName))
            FFContext.delete(this, subType.simpleName)
    }
}