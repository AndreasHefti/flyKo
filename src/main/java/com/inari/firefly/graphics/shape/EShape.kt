package com.inari.firefly.graphics.shape

import com.inari.firefly.asset.AssetInstanceRefResolver
import com.inari.firefly.component.ComponentType
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.firefly.entity.property.FloatPropertyAccessor
import com.inari.firefly.entity.property.VirtualPropertyRef
import com.inari.firefly.external.ShapeData
import com.inari.firefly.external.ShapeType
import com.inari.firefly.graphics.BlendMode
import com.inari.util.graphics.RGBColor
import java.util.*

class EShape private constructor(): EntityComponent(EShape::class.java.name) {

    @JvmField val data = ShapeData()

    var ff_Type: ShapeType
        get() = data.type
        set(value) { data.type = value }
    var ff_Vertices: FloatArray
        get() = data.vertices
        set(value) { data.vertices = value }
    var ff_Color: RGBColor
        get() = data.color1
        set(value) = data.color1(value)
    var ff_GradientColor1: RGBColor
        get() = data.color2!!
        set(value) {data.color2 = value}
    var ff_GradientColor2: RGBColor
        get() = data.color3!!
        set(value) {data.color3 = value}
    var ff_GradientColor3: RGBColor
        get() = data.color4!!
        set(value) {data.color4 = value}
    var ff_Segments: Int
        get() = data.segments
        set(value) { data.segments = value }
    var ff_Fill: Boolean
        get() = data.fill
        set(value) { data.fill = value }
    var ff_Blend: BlendMode
        get() = data.blend
        set(value) { data.blend = value }
    val ff_Shader = AssetInstanceRefResolver(
        { index -> data.shaderRef = index },
        { data.shaderRef })

    override fun reset() {
        data.reset()
    }

    override fun toString(): String {
        return "EShape(subType=$data.subType, " +
            "vertices=${Arrays.toString(data.vertices)}, " +
            "color1=$data.color1, " +
            "color2=$data.color2, " +
            "color3=$data.color3, " +
            "color4=$data.color4, " +
            "segments=$data.segments, " +
            "fill=$data.fill, " +
            "blend=$data.blend, " +
            "shaderRef=$data.shaderRef, " +
            "ff_ShaderAsset=$ff_Shader)"
    }

    private val accessorColorRed: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {data.color1.r = value}
        override fun get(): Float = data.color1.r
    }
    private val accessorColorGreen: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {data.color1.g = value}
        override fun get(): Float = data.color1.g
    }
    private val accessorColorBlue: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {data.color1.b = value}
        override fun get(): Float = data.color1.b
    }
    private val accessorColorAlpha: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {data.color1.a = value}
        override fun get(): Float = data.color1.a
    }

    enum class Property(
        override val propertyName: String,
        override val type: Class<*>
    ) : VirtualPropertyRef {
        COLOR_RED("colorRed", Float::class.java) {
            override fun accessor(entity: Entity): FloatPropertyAccessor {
                return entity[EShape].accessorColorRed
            }
        },
        COLOR_GREEN("colorGreen", Float::class.java) {
            override fun accessor(entity: Entity): FloatPropertyAccessor {
                return entity[EShape].accessorColorGreen
            }
        },
        COLOR_BLUE("colorBlue", Float::class.java) {
            override fun accessor(entity: Entity): FloatPropertyAccessor {
                return entity[EShape].accessorColorBlue
            }
        },
        COLOR_ALPHA("colorAlpha", Float::class.java) {
            override fun accessor(entity: Entity): FloatPropertyAccessor {
                return entity[EShape].accessorColorAlpha
            }
        }
    }

    override fun componentType(): ComponentType<EShape> =
        EShape.Companion

    companion object : EntityComponentType<EShape>(EShape::class.java) {
        override fun createEmpty() = EShape()
    }
}