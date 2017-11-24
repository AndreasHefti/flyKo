package com.inari.firefly.asset

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.FFContext
import com.inari.firefly.system.component.SystemComponent

abstract class Asset: SystemComponent {

    protected constructor()
    override final fun indexedTypeKey(): IIndexedTypeKey = typeKey

    protected var dependsOn: Int = -1
    fun instanceId(): Int = instanceId(0)
    abstract fun instanceId(index: Int): Int

    abstract fun load()
    abstract fun unload()
    fun loaded():Boolean = FFContext.isActive(componentId)

    protected fun checkNotAlreadyLoaded() {
        if (loaded()) {
            throw IllegalStateException("Asset: $componentId is already loaded and can not be modified");
        }
    }

    companion object : ComponentType<Asset> {
        override val typeKey = SystemComponent.createTypeKey(Asset::class.java)
    }
}