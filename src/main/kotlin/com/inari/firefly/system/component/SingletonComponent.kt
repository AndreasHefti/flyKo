package com.inari.firefly.system.component

import com.inari.firefly.FFContext
import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.component.CompId

abstract class SingletonComponent<C : SystemComponent, CC : C>(
    baseType: SystemComponentType<C>,
    subTypeClass: Class<CC>
): SystemComponentSubType<C, CC>(baseType, subTypeClass) {

    override fun createEmpty(): CC = throw UnsupportedOperationException()
    protected abstract fun create(): CC

    val id: CompId
        get() {
            return if (FFContext.mapper<CC>(this).contains(subTypeClass.simpleName))
                 FFContext[this, subTypeClass.simpleName].componentId
            else NO_COMP_ID
        }

    val instance: CC
        get() {
            if (!FFContext.mapper<CC>(this).contains(subTypeClass.simpleName)) {
                val comp = create()
                comp.ff_Name = subTypeClass.simpleName
                FFContext.mapper<CC>(this).receiver()(comp)
            }

            @Suppress("UNCHECKED_CAST")
            return FFContext[this, subTypeClass.simpleName]
        }

    fun dispose() {
        if (FFContext.mapper<CC>(this).contains(subTypeClass.simpleName))
            FFContext.delete(this, subTypeClass.simpleName)
    }
}