package com.inari.firefly.graphics.tile

import com.inari.commons.geom.Position
import com.inari.commons.graphics.RGBColor
import com.inari.commons.lang.list.DynArray
import com.inari.firefly.asset.AssetInstanceRefResolver
import com.inari.firefly.component.ArrayAccessor
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.external.SpriteRenderable
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.setFrom

class ETile private constructor (

) : EntityComponent(), SpriteRenderable {

    @JvmField internal var spriteRef = -1
    @JvmField internal var shaderRef = -1
    @JvmField internal var blend: BlendMode = BlendMode.NONE
    @JvmField internal var tint: RGBColor = RGBColor(1f, 1f, 1f, 1f)
    @JvmField internal var positions: DynArray<Position> = DynArray.create(Position::class.java)

    val ff_Sprite =
        AssetInstanceRefResolver({ index -> spriteRef = setIfNotInitialized(index, "ff_SpriteAsset") })
    val ff_Shader =
        AssetInstanceRefResolver({ index -> shaderRef = setIfNotInitialized(index, "ff_ShaderAsset") })
    var ff_Blend: BlendMode
        get() = blend
        set(value) { blend = value }
    var ff_Tint: RGBColor
        get() = tint
        set(value) { tint.setFrom(value) }
    val ff_Positions =
        ArrayAccessor(positions)

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

    override fun indexedTypeKey() = ESprite.typeKey

    companion object : EntityComponentType<ETile>() {
        override val typeKey = EntityComponent.createTypeKey(ETile::class.java)
        override fun createEmpty() = ETile()
    }
}