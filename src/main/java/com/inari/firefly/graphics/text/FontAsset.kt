package com.inari.firefly.graphics.text

import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.commons.lang.list.IntBag
import com.inari.firefly.*
import com.inari.firefly.asset.Asset
import com.inari.firefly.external.SpriteData
import com.inari.firefly.external.TextureData
import com.inari.firefly.system.component.SubType


class FontAsset : Asset(), TextureData {

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
    override val colorConverter: IntFunction = NULL_INT_FUNCTION

    @JvmField internal var charMap: Array<CharArray> = emptyArray()
    @JvmField internal var charWidth = 0
    @JvmField internal var charHeight = 0
    @JvmField internal var charSpace = 0
    @JvmField internal var lineSpace = 0
    @JvmField internal var defaultChar = -1

    internal val charSpriteMap = IntBag(256, -1)
    private var texId = -1

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


    override fun instanceId(index: Int): Int =
        throw UnsupportedOperationException()

    override fun load() {
        val graphics = FFContext.graphics

        texId = graphics.createTexture(this).first
        tmpSpriteData.rect(0, 0, charWidth, charHeight)
        for (y in 0 until charMap.size) {
            for (x in 0 until charMap[y].size) {
                tmpSpriteData.rect.x = x * charWidth
                tmpSpriteData.rect.y = y * charHeight

                val charSpriteId = graphics.createSprite(tmpSpriteData)
                charSpriteMap.set(charMap[y][x].toInt(), charSpriteId)
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

        graphics.disposeTexture(texId)
        texId = -1
    }

    companion object : SubType<FontAsset, Asset>() {
        override val typeKey: IndexedTypeKey = Asset.typeKey
        override fun subType() = FontAsset::class.java
        override fun createEmpty() = FontAsset()
    }

    private val tmpSpriteData = object : SpriteData {
        var rect = Rectangle()

        override val textureId get() = texId
        override val x get() = rect.x
        override val y get() = rect.y
        override val width get() = rect.width
        override val height get() = rect.height
        override val isHorizontalFlip = false
        override val isVerticalFlip = false
    }
}