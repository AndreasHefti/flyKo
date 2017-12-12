package com.inari.firefly.graphics.sprite

import com.inari.commons.graphics.RGBColor
import com.inari.firefly.asset.AssetInstanceRefResolver
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.external.SpriteRenderable
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.setFrom


class ESprite private constructor () : EntityComponent(), SpriteRenderable {

    @JvmField internal var spriteRef = -1
    @JvmField internal var shaderRef = -1
    @JvmField internal var blend = BlendMode.NONE
    @JvmField internal val tint = RGBColor(1f, 1f, 1f, 1f)

    val ff_Sprite =
        AssetInstanceRefResolver({ index -> spriteRef = setIfNotInitialized(index, "ff_Sprite") })
    val ff_Shader =
        AssetInstanceRefResolver({ index -> shaderRef = setIfNotInitialized(index, "ff_Shader") })
    var ff_Blend: BlendMode
        get() = blend
        set(value) { blend = value }
    var ff_Tint: RGBColor
        get() = tint
        set(value) { tint.setFrom(value) }

    override val spriteId: Int
        get() = spriteRef
    override val tintColor: RGBColor
        get() = tint
    override val blendMode: BlendMode
        get() = blend
    override val shaderId: Int
        get() = shaderRef

    override fun reset() {
        ff_Tint.r = 1f
        ff_Tint.g = 1f
        ff_Tint.b = 1f
        ff_Tint.a = 1f
        ff_Blend = BlendMode.NONE
        spriteRef = -1
        shaderRef = -1
    }

    override fun toString(): String {
        return "ESprite(spriteRef=$spriteRef, " +
            "shaderRef=$shaderRef, " +
            "blend=$blend, " +
            "tint=$tint, " 
    }

    override fun indexedTypeKey() = typeKey
    companion object : EntityComponentType<ESprite>() {
        override val typeKey = EntityComponent.createTypeKey(ESprite::class.java)
        override fun createEmpty() = ESprite()
    }
}