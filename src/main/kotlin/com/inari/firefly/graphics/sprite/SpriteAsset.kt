package com.inari.firefly.graphics.sprite

import com.inari.firefly.FFContext
import com.inari.firefly.asset.Asset
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.external.SpriteData
import com.inari.firefly.graphics.TextureAsset
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.geom.Rectangle

class SpriteAsset private constructor() : Asset() {

    @JvmField internal var textureAssetRef = -1
    @JvmField internal var spriteId: Int = -1
    @JvmField internal val spriteData = SpriteData()

    var texture =
        ComponentRefResolver(Asset) { index-> run {
            dependingRef = setIfNotInitialized(index, "TextureAsset")
            textureAssetRef = index
        } }
    var textureRegion: Rectangle
        get() = spriteData.region
        set(value) { spriteData.region(value) }
    var horizontalFlip: Boolean
        get() = spriteData.isHorizontalFlip
        set(value) { spriteData.isHorizontalFlip = value }
    var verticalFlip: Boolean
        get() = spriteData.isVerticalFlip
        set(value) { spriteData.isVerticalFlip = value }

    override fun instanceId(index: Int): Int = spriteId

    override fun load() {
        if (spriteId >= 0)
            return

        FFContext.activate(TextureAsset, textureAssetRef)
        spriteData.textureId = FFContext[TextureAsset, textureAssetRef].instanceId
        spriteId = FFContext.graphics.createSprite(spriteData)
    }

    override fun unload() {
        if (spriteId >= 0) {
            FFContext.graphics.disposeSprite(spriteId)
            spriteId = -1
        }
    }

    companion object : SystemComponentSubType<Asset, SpriteAsset>(Asset, SpriteAsset::class) {
        override fun createEmpty() = SpriteAsset()
    }
}