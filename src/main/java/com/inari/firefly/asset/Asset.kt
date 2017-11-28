package com.inari.firefly.asset

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.firefly.component.ComponentType
import com.inari.firefly.component.NamedReference
import com.inari.firefly.FFContext
import com.inari.firefly.system.component.SystemComponent

abstract class Asset protected constructor(): SystemComponent() {

    override final fun indexedTypeKey(): IIndexedTypeKey = typeKey

    protected val depending = NamedReference(Asset)
    fun dependingIndex(): Int = depending.index
    fun dependsOn(index: Int): Boolean = depending.index == index
    fun dependsOn(name: String): Boolean = depending.name == name

    fun instanceId(): Int = instanceId(0)
    abstract fun instanceId(index: Int): Int

    abstract fun load()
    abstract fun unload()
    fun loaded():Boolean = FFContext.isActive(componentId)

    protected fun checkNotAlreadyLoaded() {
        if (loaded()) {
            throw IllegalStateException("Asset: $componentId is already loaded and can not be modified")
        }
    }

    companion object : ComponentType<Asset> {
        override val typeKey = SystemComponent.createTypeKey(Asset::class.java)
    }
}