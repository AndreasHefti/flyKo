package com.inari.firefly.graphics.sprite

import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.FFContext
import com.inari.firefly.asset.Asset
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.external.SpriteData
import com.inari.firefly.system.component.SubType

class SpriteAsset private constructor() : Asset(), SpriteData {

    @JvmField internal var spriteId: Int = -1

    @JvmField internal var region: Rectangle = Rectangle(0, 0, 0, 0)
    @JvmField internal var horizontalFlip: Boolean = false
    @JvmField internal var verticalFlip: Boolean = false

    var ff_Texture =
        ComponentRefResolver(Asset, { index-> dependingRef = setIfNotInitialized(index, "ff_Texture") })
    var ff_TextureRegion: Rectangle
        get() = region
        set(value) { region.setFrom(value) }
    var ff_HorizontalFlip: Boolean
        get() = horizontalFlip
        set(value) { horizontalFlip = value }
    var ff_VerticalFlip: Boolean
        get() = verticalFlip
        set(value) { verticalFlip = value }

    override val textureId: Int
        get() = dependingRef
    override val textureRegion: Rectangle
        get() = region
    override val isHorizontalFlip: Boolean
        get() = horizontalFlip
    override val isVerticalFlip: Boolean
        get() = verticalFlip

    override fun instanceId(index: Int): Int = spriteId

    override fun load() {
        if (spriteId < 0)
            spriteId = FFContext.graphics.createSprite(this)
    }

    override fun unload() {
        if (spriteId >= 0) {
            FFContext.graphics.disposeSprite(spriteId)
            spriteId = -1
        }
    }


    companion object : SubType<SpriteAsset, Asset>() {
        override val typeKey: IndexedTypeKey = Asset.typeKey
        override fun subType() = SpriteAsset::class.java
        override fun createEmpty() = SpriteAsset()
    }
}