package com.inari.firefly.graphics.sprite

import com.inari.firefly.asset.AssetInstanceRefResolver
import com.inari.firefly.component.ComponentType
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.property.FloatPropertyAccessor
import com.inari.firefly.entity.property.IntPropertyAccessor
import com.inari.firefly.entity.property.VirtualPropertyRef
import com.inari.firefly.external.SpriteRenderable
import com.inari.firefly.graphics.BlendMode
import com.inari.util.graphics.RGBColor


class ESprite private constructor () : EntityComponent(), SpriteRenderable {

    @JvmField internal var spriteRef = -1
    @JvmField internal var shaderRef = -1
    @JvmField internal var blend = BlendMode.NONE
    @JvmField internal val tint = RGBColor(1f, 1f, 1f, 1f)

    val ff_Sprite = AssetInstanceRefResolver({ index -> spriteRef = index })
    val ff_Shader = AssetInstanceRefResolver({ index -> shaderRef = index })
    var ff_Blend: BlendMode
        get() = blend
        set(value) { blend = value }
    var ff_Tint: RGBColor
        get() = tint
        set(value) { tint(value) }

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

    override fun componentType(): ComponentType<ESprite> =
        ESprite.Companion

    companion object : EntityComponentType<ESprite>() {
        override val indexedTypeKey by lazy { EntityComponent.create(ESprite::class.java) }
        override fun createEmpty() = ESprite()
    }
}