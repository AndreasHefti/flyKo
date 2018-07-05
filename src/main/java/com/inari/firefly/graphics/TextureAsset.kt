package com.inari.firefly.graphics

import com.inari.firefly.FFContext
import com.inari.firefly.IntFunction
import com.inari.firefly.NO_NAME
import com.inari.firefly.NULL_INT_FUNCTION
import com.inari.firefly.asset.Asset
import com.inari.firefly.external.TextureData
import com.inari.firefly.system.component.SubType

class TextureAsset private constructor() : Asset(), TextureData {

    @JvmField internal var id: Int = -1
    override fun instanceId(index: Int): Int = id

    var width: Int = -1
        private set
    var height: Int = -1
        private set

    override var resourceName: String = NO_NAME
        private set
    override var isMipmap: Boolean = false
        private set
    override var wrapS: Int = -1
        private set
    override var wrapT: Int = -1
        private set
    override var minFilter: Int = -1
        private set
    override var magFilter: Int = -1
        private set
    override var colorConverter: IntFunction = NULL_INT_FUNCTION
        private set

    var ff_ResourceName: String
        get() = resourceName
        set(value) {resourceName = setIfNotInitialized(value, "ff_ResourceName")}
    var ff_MipMap
        get() = isMipmap
        set(value) {isMipmap = setIfNotInitialized(value, "ff_MipMap")}
    var ff_WrapS
        get() = wrapS
        set(value) {wrapS = setIfNotInitialized(value, "ff_WrapS")}
    var ff_WrapT
        get() = wrapT
        set(value) {wrapT = setIfNotInitialized(value, "ff_WrapT")}
    var ff_MinFilter
        get() = minFilter
        set(value) {minFilter = setIfNotInitialized(value, "ff_MinFilter")}
    var ff_MagFilter
        get() = magFilter
        set(value) {magFilter = setIfNotInitialized(value, "ff_MagFilter")}
    var ff_ColorConverter
        get() = colorConverter
        set(value) {colorConverter = setIfNotInitialized(value, "ff_ColorConverter")}

    override fun load() {
        if (id < 0) {
            val textData = FFContext.graphics.createTexture(this)
            id = textData.first
            width = textData.second
            height = textData.third
        }
    }

    override fun unload() {
        if (id >= 0) {
            FFContext.graphics.disposeTexture(id)
            id = -1
            width = -1
            height = -1
        }
    }

    companion object : SubType<TextureAsset, Asset>() {
        override val indexedTypeKey = Asset.indexedTypeKey
        override val subType = TextureAsset::class.java
        override fun createEmpty() = TextureAsset()
    }
}