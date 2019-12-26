package com.inari.firefly.graphics.sprite

import com.inari.firefly.asset.AssetInstanceRefResolver
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.firefly.entity.property.FloatPropertyAccessor
import com.inari.firefly.entity.property.IntPropertyAccessor
import com.inari.firefly.entity.property.VirtualPropertyRef
import com.inari.firefly.external.SpriteRenderable
import com.inari.firefly.graphics.BlendMode
import com.inari.util.graphics.RGBColor


class ESprite private constructor () : EntityComponent(ESprite::class.java.name) {

    @JvmField internal val spriteRenderable = SpriteRenderable()

    val ff_Sprite = AssetInstanceRefResolver(
        { index -> spriteRenderable.spriteId = index },
        { spriteRenderable.spriteId })
    val ff_Shader = AssetInstanceRefResolver(
        { index -> spriteRenderable.shaderId = index },
        { spriteRenderable.spriteId })
    var ff_Blend: BlendMode
        get() = spriteRenderable.blendMode
        set(value) { spriteRenderable.blendMode = value }
    var ff_Tint: RGBColor
        get() = spriteRenderable.tintColor
        set(value) { spriteRenderable.tintColor(value) }

    override fun reset() {
        spriteRenderable.reset()
    }

    override fun toString(): String {
        return "ESprite(spriteRef=${spriteRenderable.spriteId}, " +
            "shaderRef=${spriteRenderable.shaderId}, " +
            "blend=${spriteRenderable.blendMode}, " +
            "tint=${spriteRenderable.tintColor}, "
    }

    private val accessorSpriteRef: IntPropertyAccessor = object : IntPropertyAccessor {
        override fun set(value: Int) {spriteRenderable.spriteId = value}
        override fun get(): Int = spriteRenderable.spriteId
    }
    private val accessorTintRed: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {spriteRenderable.tintColor.r = value}
        override fun get(): Float = spriteRenderable.tintColor.r
    }
    private val accessorTintGreen: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {spriteRenderable.tintColor.g = value}
        override fun get(): Float = spriteRenderable.tintColor.g
    }
    private val accessorTintBlue: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {spriteRenderable.tintColor.b = value}
        override fun get(): Float = spriteRenderable.tintColor.b
    }
    private val accessorTintAlpha: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {spriteRenderable.tintColor.a = value}
        override fun get(): Float = spriteRenderable.tintColor.a
    }

    enum class Property(
        override val propertyName: String,
        override val type: Class<*>
    ) : VirtualPropertyRef {
        SPRITE_REFERENCE("spriteRef", Int::class.java) {
            override fun accessor(entity: Entity): IntPropertyAccessor {
                return entity[ESprite].accessorSpriteRef
            }
        },
        TINT_RED("tintRed", Float::class.java) {
            override fun accessor(entity: Entity): FloatPropertyAccessor {
                return entity[ESprite].accessorTintRed
            }
        },
        TINT_GREEN("tintGreen", Float::class.java) {
            override fun accessor(entity: Entity): FloatPropertyAccessor {
                return entity[ESprite].accessorTintGreen
            }
        },
        TINT_BLUE("tintBlue", Float::class.java) {
            override fun accessor(entity: Entity): FloatPropertyAccessor {
                return entity[ESprite].accessorTintBlue
            }
        },
        TINT_ALPHA("tintAlpha", Float::class.java) {
            override fun accessor(entity: Entity): FloatPropertyAccessor {
                return entity[ESprite].accessorTintAlpha
            }
        }
    }

    override fun componentType() =
        ESprite.Companion

    companion object : EntityComponentType<ESprite>(ESprite::class.java) {
        override fun createEmpty() = ESprite()
    }
}