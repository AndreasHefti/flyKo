package com.inari.firefly.graphics.sprite

import com.inari.firefly.FFContext
import com.inari.firefly.asset.Asset
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.external.SpriteData
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.geom.Rectangle

class SpriteAsset private constructor() : Asset(), SpriteData {

    @JvmField internal var spriteId: Int = -1

    @JvmField internal var region = Rectangle()
    @JvmField internal var horizontalFlip: Boolean = false
    @JvmField internal var verticalFlip: Boolean = false

    var ff_Texture =
        ComponentRefResolver(Asset) { index-> dependingRef = setIfNotInitialized(index, "ff_Texture") }
    var ff_TextureRegion: Rectangle
        get() = region
        set(value) { region(value) }
    var ff_HorizontalFlip: Boolean
        get() = horizontalFlip
        set(value) { horizontalFlip = value }
    var ff_VerticalFlip: Boolean
        get() = verticalFlip
        set(value) { verticalFlip = value }

    override val textureId: Int get() = dependingRef
    override val x: Int get() = region.pos.x
    override val y: Int get() = region.pos.y
    override val width: Int get() = region.width
    override val height: Int get() = region.height
    override val isHorizontalFlip: Boolean get() = horizontalFlip
    override val isVerticalFlip: Boolean get() = verticalFlip

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

    companion object : SystemComponentSubType<Asset, SpriteAsset>(Asset, SpriteAsset::class.java) {
        override fun createEmpty() = SpriteAsset()
    }
}