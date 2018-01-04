package com.inari.firefly.graphics.tile

import com.inari.commons.geom.Position
import com.inari.commons.graphics.RGBColor
import com.inari.commons.lang.list.DynArray
import com.inari.firefly.asset.AssetInstanceRefResolver
import com.inari.firefly.component.ArrayAccessor
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.property.FloatPropertyAccessor
import com.inari.firefly.entity.property.IntPropertyAccessor
import com.inari.firefly.entity.property.VirtualPropertyRef
import com.inari.firefly.external.SpriteRenderable
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.setFrom

class ETile private constructor (

) : EntityComponent(), SpriteRenderable {

    @JvmField internal var spriteRef = -1
    @JvmField internal var shaderRef = -1
    @JvmField internal var blend: BlendMode = BlendMode.NONE
    @JvmField internal var tint: RGBColor = RGBColor(1f, 1f, 1f, 1f)
    @JvmField internal var positions: DynArray<Position> = DynArray.create(Position::class.java)

    val ff_Sprite =
        AssetInstanceRefResolver({ index -> spriteRef = index })
    val ff_Shader =
        AssetInstanceRefResolver({ index -> shaderRef = index })
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

    private val accessorSpriteRef: IntPropertyAccessor = object : IntPropertyAccessor {
        override fun set(value: Int) {spriteRef = value}
        override fun get(): Int = spriteRef
    }
    private val accessorTintRed: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {tint.r = value}
        override fun get(): Float = tint.r
    }
    private val accessorTintGreen: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {tint.g = value}
        override fun get(): Float = tint.g
    }
    private val accessorTintBlue: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {tint.b = value}
        override fun get(): Float = tint.b
    }
    private val accessorTintAlpha: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {tint.a = value}
        override fun get(): Float = tint.a
    }

    enum class Property(
        override val propertyName: String,
        override val type: Class<*>
    ) : VirtualPropertyRef {
        SPRITE_REFERENCE("spriteRef", Int::class.java) {
            override fun accessor(entity: Entity): IntPropertyAccessor {
                return entity[ETile].accessorSpriteRef
            }
        },
        TINT_RED("tintRed", Float::class.java) {
            override fun accessor(entity: Entity): FloatPropertyAccessor {
                return entity[ETile].accessorTintRed
            }
        },
        TINT_GREEN("tintGreen", Float::class.java) {
            override fun accessor(entity: Entity): FloatPropertyAccessor {
                return entity[ETile].accessorTintGreen
            }
        },
        TINT_BLUE("tintBlue", Float::class.java) {
            override fun accessor(entity: Entity): FloatPropertyAccessor {
                return entity[ETile].accessorTintBlue
            }
        },
        TINT_ALPHA("tintAlpha", Float::class.java) {
            override fun accessor(entity: Entity): FloatPropertyAccessor {
                return entity[ETile].accessorTintAlpha
            }
        }
    }

    override fun indexedTypeKey() = ETile.typeKey
    companion object : EntityComponentType<ETile>() {
        override val typeKey = EntityComponent.createTypeKey(ETile::class.java)
        override fun createEmpty() = ETile()
    }
}