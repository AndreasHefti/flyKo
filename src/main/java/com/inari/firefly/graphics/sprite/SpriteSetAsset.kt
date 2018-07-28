package com.inari.firefly.graphics.sprite

import com.inari.firefly.FFContext
import com.inari.firefly.NO_NAME
import com.inari.firefly.asset.Asset
import com.inari.firefly.component.ArrayAccessor
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.external.SpriteData
import com.inari.firefly.graphics.sprite.Sprite.Companion.NULL_SPRITE
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.collection.DynArray


class SpriteSetAsset private constructor() : Asset() {

    private val spriteData = DynArray.of(Sprite::class.java, 30)

    val ff_SpriteData = ArrayAccessor(spriteData)
    var ff_Texture = ComponentRefResolver(Asset) { index -> dependingRef = setIfNotInitialized(index, "ff_Texture") }

    override fun instanceId(index: Int): Int =
        spriteData[index]?.instanceId ?: -1

    fun instanceId(name: String): Int {
        if (name == NO_NAME)
            return -1

        spriteData
            .filter { name == it.name }
            .forEach { return it.instanceId }

        return -1
    }

    override fun load() {
        val graphics = FFContext.graphics
        spriteDataContainer.spriteData.textureId = FFContext.assetInstanceId(dependingRef)
        for (i in 0 until spriteData.capacity) {
            val sprite = spriteData[i] ?: continue
            spriteDataContainer.sprite = sprite
            sprite.instId = graphics.createSprite(spriteDataContainer.spriteData)
        }
    }

    override fun unload() {
        val graphics = FFContext.graphics
        for (i in 0 until spriteData.capacity) {
            val sprite = spriteData[i] ?: continue
            graphics.disposeSprite(sprite.instanceId)
            sprite.instId = -1
        }

        spriteDataContainer.spriteData.textureId = -1
        spriteDataContainer.sprite = NULL_SPRITE
    }

    private val spriteDataContainer = object : Any() {
        @JvmField val spriteData = SpriteData()
        @JvmField var sprite: Sprite = NULL_SPRITE

    }

    companion object : SystemComponentSubType<Asset, SpriteSetAsset>(Asset, SpriteSetAsset::class.java) {
        override fun createEmpty() = SpriteSetAsset()
    }
}