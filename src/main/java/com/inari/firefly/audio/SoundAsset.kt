package com.inari.firefly.audio

import com.inari.firefly.FFContext
import com.inari.firefly.NO_NAME
import com.inari.firefly.asset.Asset
import com.inari.firefly.system.component.SystemComponentSubType

class SoundAsset private constructor() : Asset() {

    @JvmField internal var id: Int = -1
    override fun instanceId(index: Int): Int = id
    val soundId get() = id

    @JvmField internal var instance: Int = -1
    @JvmField internal var resourceName: String = NO_NAME
    @JvmField internal var streaming: Boolean = false

    var ff_ResourceName: String
        get() = resourceName
        set(value) {resourceName = setIfNotInitialized(value, "ff_ResourceName")}
    var ff_Streaming
        get() = streaming
        set(value) {streaming = setIfNotInitialized(value, "ff_Streaming")}

    override fun load() {
        if (id < 0)
            id = FFContext.audio.createSound(resourceName, ff_Streaming)
    }

    override fun unload() {
        if (id >= 0)
            FFContext.audio.disposeSound(id)
        id = -1
    }

    companion object : SystemComponentSubType<Asset, SoundAsset>(Asset, SoundAsset::class.java) {
        override fun createEmpty() = SoundAsset()
    }
}