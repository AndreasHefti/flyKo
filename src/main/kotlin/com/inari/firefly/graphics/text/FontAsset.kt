package com.inari.firefly.graphics.text

import com.inari.firefly.*
import com.inari.firefly.asset.Asset
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.external.SpriteData
import com.inari.firefly.external.TextureData
import com.inari.firefly.graphics.TextureAsset
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.collection.DynIntArray


class FontAsset : Asset() {

    //@JvmField internal val textureData = TextureData()

    @JvmField internal var textureAssetId = NO_COMP_ID
    @JvmField internal var charMap: Array<CharArray> = emptyArray()
    @JvmField internal var charWidth = 0
    @JvmField internal var charHeight = 0
    @JvmField internal var charSpace = 0
    @JvmField internal var lineSpace = 0
    @JvmField internal var defaultChar = -1
    @JvmField internal var xOffset = 0
    @JvmField internal var yOffset = 0

    internal val charSpriteMap = DynIntArray(256, -1)
    private val tmpSpriteData = SpriteData()

    var ff_Texture =
            ComponentRefResolver(Asset) { index-> run {
                dependingRef = setIfNotInitialized(index, "ff_TextureAsset")
                textureAssetId = CompId(index, TextureAsset)
            } }
//    var ff_ResourceName: String
//        get() = textureData.resourceName
//        set(value) {textureData.resourceName = setIfNotInitialized(value, "ff_ResourceName")}
//    var ff_MipMap
//        get() = textureData.isMipmap
//        set(value) {textureData.isMipmap = setIfNotInitialized(value, "ff_MipMap")}
//    var ff_WrapS
//        get() = textureData.wrapS
//        set(value) {textureData.wrapS = setIfNotInitialized(value, "ff_WrapS")}
//    var ff_WrapT
//        get() = textureData.wrapT
//        set(value) {textureData.wrapT = setIfNotInitialized(value, "ff_WrapT")}
//    var ff_MinFilter
//        get() = textureData.minFilter
//        set(value) {textureData.minFilter = setIfNotInitialized(value, "ff_MinFilter")}
//    var ff_MagFilter
//        get() = textureData.magFilter
//        set(value) {textureData.magFilter = setIfNotInitialized(value, "ff_MagFilter")}
    var ff_CharMap
        get() = charMap
        set(value) {charMap = setIfNotInitialized(value, "ff_CharMap")}
    var ff_CharWidth
        get() = charWidth
        set(value) {charWidth = setIfNotInitialized(value, "ff_CharWidth")}
    var ff_CharHeight
        get() = charHeight
        set(value) {charHeight = setIfNotInitialized(value, "ff_CharHeight")}
    var ff_CharSpace
        get() = charSpace
        set(value) {charSpace = setIfNotInitialized(value, "ff_CharSpace")}
    var ff_LineSpace
        get() = lineSpace
        set(value) {lineSpace = setIfNotInitialized(value, "ff_LineSpace")}
    var ff_DefaultChar : Char
        get() = defaultChar.toChar()
        set(value) {defaultChar = setIfNotInitialized(value.toInt(), "ff_DefaultChar")}
    var ff_XOffset
        get() = xOffset
        set(value) {xOffset = setIfNotInitialized(value, "ff_XOffset")}
    var ff_YOffset
        get() = yOffset
        set(value) {yOffset = setIfNotInitialized(value, "ff_YOffset")}


    override fun instanceId(index: Int): Int =
        throw UnsupportedOperationException()

    override fun load() {
        val graphics = FFContext.graphics
        FFContext.activate(textureAssetId)
        val texture: TextureAsset = FFContext[textureAssetId]

        tmpSpriteData.textureId = texture.instanceId
        tmpSpriteData.region(0, 0, charWidth, charHeight)
        for (y in charMap.indices) {
            for (x in charMap[y].indices) {
                tmpSpriteData.region(
                        x * charWidth + xOffset,
                        y * charHeight + yOffset)

                val charSpriteId = graphics.createSprite(tmpSpriteData)
                charSpriteMap[charMap[y][x].toInt()] = charSpriteId
            }
        }
    }

    operator fun get(char: Char): Int {
        val index = char.toInt()
        return if (charSpriteMap.isEmpty(index))
            defaultChar
        else
            charSpriteMap[index]
    }

    override fun unload() {
        val graphics = FFContext.graphics

        val iterator = charSpriteMap.iterator()
        while (iterator.hasNext()) {
            graphics.disposeSprite(iterator.next())
        }
        charSpriteMap.clear()

        graphics.disposeTexture(tmpSpriteData.textureId)
        tmpSpriteData.textureId = -1
    }

    companion object : SystemComponentSubType<Asset, FontAsset>(Asset, FontAsset::class.java) {
        override fun createEmpty() = FontAsset()
    }

}