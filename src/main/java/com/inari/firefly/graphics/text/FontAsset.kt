package com.inari.firefly.graphics.text

import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.commons.lang.list.IntBag
import com.inari.firefly.FFContext
import com.inari.firefly.NO_NAME
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
    override val colorConverter: (Int) -> Int
        get() = null!! // TODO

    @JvmField internal var charMap: Array<CharArray> = emptyArray()
    @JvmField internal var charWidth = 0
    @JvmField internal var charHeight = 0
    @JvmField internal var charSpace = 0
    @JvmField internal var lineSpace = 0
    @JvmField internal var defaultChar = -1

    internal val charSpriteMap = IntBag(256, -1)
    private var textureId = -1

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

    override fun instanceId(index: Int): Int =
        throw UnsupportedOperationException()

    override fun load() {
        val graphics = FFContext.graphics

        textureId = graphics.createTexture(this).first
        val textureRegion = Rectangle(0, 0, charWidth, charHeight)
        val spriteData = InternalSpriteData(this, textureRegion)

        for (y in 0 until charMap.size) {
            for (x in 0 until charMap[y].size) {
                textureRegion.x = x * charWidth
                textureRegion.y = y * charHeight

                val charSpriteId = graphics.createSprite(spriteData)
                charSpriteMap.set(charMap[y][x].toInt(), charSpriteId)
            }
        }
    }

    override fun unload() {
        val graphics = FFContext.graphics

        val iterator = charSpriteMap.iterator()
        while (iterator.hasNext()) {
            graphics.disposeSprite(iterator.next())
        }
        charSpriteMap.clear()

        graphics.disposeTexture(textureId)
        textureId = -1
    }

    companion object : SubType<FontAsset, Asset>() {
        override val typeKey: IndexedTypeKey = Asset.typeKey
        override fun subType() = FontAsset::class.java
        override fun createEmpty() = FontAsset()
    }

    private inner class InternalSpriteData(
        fontAsset: FontAsset,
        override val textureRegion: Rectangle
    ) : SpriteData {
        override val textureId: Int = fontAsset.textureId
        override val isHorizontalFlip: Boolean = false
        override val isVerticalFlip: Boolean = false
    }
}