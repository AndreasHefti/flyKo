package com.inari.firefly.graphics.sprite

import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.commons.lang.list.DynArray
import com.inari.firefly.FFContext
import com.inari.firefly.NO_NAME
import com.inari.firefly.asset.Asset
import com.inari.firefly.component.ArrayAccessor
import com.inari.firefly.external.SpriteData
import com.inari.firefly.system.component.SubType


class SpriteSetAsset private constructor() : Asset() {

    private val spriteData = DynArray.create(Sprite::class.java, 30)

    val ff_SpriteData = ArrayAccessor<Sprite>(spriteData)

    override fun instanceId(index: Int): Int =
        if (index in spriteData) spriteData[index].instanceId
        else -1

    override fun load() {
        val graphics = FFContext.graphics
        spriteDataContainer.texId = FFContext.assetInstanceId(dependingRef)
        for (i in 0 until spriteData.capacity()) {
            val sprite = spriteData.get(i) ?: continue
            spriteDataContainer.sprite = sprite
            sprite.instId = graphics.createSprite(spriteDataContainer)
        }
    }

    override fun unload() {
        val graphics = FFContext.graphics
        for (i in 0 until spriteData.capacity()) {
            val sprite = spriteData.get(i) ?: continue
            graphics.disposeSprite(sprite.instanceId)
            sprite.instId = -1
        }

        spriteDataContainer.texId = -1
        spriteDataContainer.sprite = NULL_SPRITE
    }

    private val spriteDataContainer = object : SpriteData {

        @JvmField var texId = -1
        @JvmField var sprite: Sprite = NULL_SPRITE

        override val textureId: Int get() = texId
        override val x: Int get() = sprite.x
        override val y: Int get() = sprite.y
        override val width: Int get() = sprite.width
        override val height: Int get() = sprite.height
        override val isHorizontalFlip: Boolean get() = sprite.flipH
        override val isVerticalFlip: Boolean get() = sprite.flipV
    }

    class Sprite(
        @JvmField val x: Int,
        @JvmField val y: Int,
        @JvmField val width: Int,
        @JvmField val height: Int,
        @JvmField val flipH: Boolean = false,
        @JvmField val flipV: Boolean = false,
        @JvmField val name: String = NO_NAME
    ) {
        @JvmField internal var instId = -1
        val instanceId: Int get() = instId
    }

    companion object : SubType<SpriteSetAsset, Asset>() {
        override val typeKey: IndexedTypeKey = Asset.typeKey
        override fun subType() = SpriteSetAsset::class.java
        override fun createEmpty() = SpriteSetAsset()

        @JvmField val NULL_SPRITE = Sprite(0, 0, 0, 0)
    }
}