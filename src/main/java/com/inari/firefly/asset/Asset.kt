package com.inari.firefly.asset

import com.inari.firefly.FFContext
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentType

abstract class Asset protected constructor():
    SystemComponent(Asset::class.java.name),
    IndexedInstantiableList {

    @JvmField protected var dependingRef: Int = -1
    fun dependingIndex(): Int = dependingRef
    fun dependsOn(index: Int): Boolean = dependingRef == index

    override val instanceId: Int get() = instanceId(0)

    internal fun activate() = load()
    internal fun deactivate() = unload()

    protected abstract fun load()
    protected abstract fun unload()

    fun loaded():Boolean =
        FFContext.isActive(componentId)

    override fun componentType() =
        Asset.Companion

    companion object : SystemComponentType<Asset>(Asset::class.java)


}