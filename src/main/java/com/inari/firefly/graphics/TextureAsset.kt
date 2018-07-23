package com.inari.firefly.graphics

import com.inari.firefly.FFContext
import com.inari.firefly.asset.Asset
import com.inari.firefly.external.TextureData
import com.inari.firefly.system.component.SystemComponentSubType

class TextureAsset private constructor() : Asset() {

    @JvmField internal var id: Int = -1
    override fun instanceId(index: Int): Int = id

    var width: Int = -1
        private set
    var height: Int = -1
        private set

    @JvmField internal val textureData = TextureData()

    var ff_ResourceName: String
        get() = textureData.resourceName
        set(value) {textureData.resourceName = setIfNotInitialized(value, "ff_ResourceName")}
    var ff_MipMap
        get() = textureData.isMipmap
        set(value) {textureData.isMipmap = setIfNotInitialized(value, "ff_MipMap")}
    var ff_WrapS
        get() = textureData.wrapS
        set(value) {textureData.wrapS = setIfNotInitialized(value, "ff_WrapS")}
    var ff_WrapT
        get() = textureData.wrapT
        set(value) {textureData.wrapT = setIfNotInitialized(value, "ff_WrapT")}
    var ff_MinFilter
        get() = textureData.minFilter
        set(value) {textureData.minFilter = setIfNotInitialized(value, "ff_MinFilter")}
    var ff_MagFilter
        get() = textureData.magFilter
        set(value) {textureData.magFilter = setIfNotInitialized(value, "ff_MagFilter")}
    var ff_ColorConverter
        get() = textureData.colorConverter
        set(value) {textureData.colorConverter = setIfNotInitialized(value, "ff_ColorConverter")}

    override fun load() {
        if (id < 0) {
            val textData = FFContext.graphics.createTexture(textureData)
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

    companion object : SystemComponentSubType<Asset, TextureAsset>(Asset, TextureAsset::class.java) {
        override fun createEmpty() = TextureAsset()
    }
}