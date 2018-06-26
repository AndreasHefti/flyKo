package com.inari.firefly.asset

import com.inari.firefly.FFContext
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent

abstract class Asset protected constructor(): SystemComponent() {

    @JvmField protected var dependingRef: Int = -1
    fun dependingIndex(): Int = dependingRef
    fun dependsOn(index: Int): Boolean = dependingRef == index

    fun instanceId(): Int = instanceId(0)
    abstract fun instanceId(index: Int): Int

    internal fun activate() = load()
    internal fun deactivate() = unload()

    protected abstract fun load()
    protected abstract fun unload()

    fun loaded():Boolean = FFContext.isActive(componentId)

    final override fun indexedTypeKey() = typeKey
    companion object : ComponentType<Asset> {
        override val typeKey = SystemComponent.createTypeKey(Asset::class.java)
    }
}