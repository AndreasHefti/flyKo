package com.inari.firefly.graphics.shape

import com.inari.commons.graphics.RGBColor
import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.firefly.asset.AssetInstanceRefResolver
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.property.FloatPropertyAccessor
import com.inari.firefly.entity.property.VirtualPropertyRef
import com.inari.firefly.external.ShapeData
import com.inari.firefly.external.ShapeData.ShapeType
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.setFrom
import java.util.*

class EShape private constructor(): EntityComponent(), ShapeData {

    private val color = RGBColor(1f, 1f, 1f, 1f)
    private var gradientColor1: RGBColor? = null
    private var gradientColor2: RGBColor? = null
    private var gradientColor3: RGBColor? = null
    private var gradientColor4: RGBColor? = null

    override var type = ShapeType.POINT
        private set
    override var vertices = floatArrayOf()
        private set
    override val color1: RGBColor
        get() { return gradientColor1 ?: color }
    override val color2: RGBColor
        get() { return gradientColor2 ?: color }
    override val color3: RGBColor
        get() { return gradientColor3 ?: color }
    override val color4: RGBColor
        get() { return gradientColor4 ?: color }
    override var segments = 0
        private set
    override var fill = false
        private set
    override var blend = BlendMode.NONE
        private set
    override var shaderRef = -1
        private set

    var ff_Type: ShapeType
        get() = type
        set(value) { type = value }
    var ff_Vertices: FloatArray
        get() = vertices
        set(value) { vertices = value }
    var ff_Color: RGBColor
        get() = color
        set(value) = color.setFrom(value)
    var ff_GradientColor1: RGBColor
        get() = gradientColor1!!
        set(value) {gradientColor1 = value}
    var ff_GradientColor2: RGBColor
        get() = gradientColor2!!
        set(value) {gradientColor2 = value}
    var ff_GradientColor3: RGBColor
        get() = gradientColor3!!
        set(value) {gradientColor3 = value}
    var ff_GradientColor4: RGBColor
        get() = gradientColor4!!
        set(value) {gradientColor4 = value}
    var ff_Segments: Int
        get() = segments
        set(value) { segments = value }
    var ff_Fill: Boolean
        get() = fill
        set(value) { fill = value }
    var ff_Blend: BlendMode
        get() = blend
        set(value) { blend = value }
    val ff_Shader = AssetInstanceRefResolver({ index -> shaderRef = index })

    override fun reset() {
        type = ShapeType.POINT
        vertices = floatArrayOf()
        color1.r = 1f; color1.g = 1f; color1.b = 1f; color1.a = 1f
        color2.r = 1f; color2.g = 1f; color2.b = 1f; color2.a = 1f
        color3.r = 1f; color3.g = 1f; color3.b = 1f; color3.a = 1f
        color4.r = 1f; color4.g = 1f; color4.b = 1f; color4.a = 1f
        segments = 0
        fill = false
        blend = BlendMode.NONE
        shaderRef = -1
    }

    override fun toString(): String {
        return "EShape(type=$type, " +
            "vertices=${Arrays.toString(vertices)}, " +
            "color1=$color1, " +
            "color2=$color2, " +
            "color3=$color3, " +
            "color4=$color4, " +
            "segments=$segments, " +
            "fill=$fill, " +
            "blend=$blend, " +
            "shaderRef=$shaderRef, " +
            "ff_Shader=$ff_Shader)"
    }

    private val accessorColorRed: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {color1.r = value}
        override fun get(): Float = color1.r
    }
    private val accessorColorGreen: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {color1.g = value}
        override fun get(): Float = color1.g
    }
    private val accessorColorBlue: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {color1.b = value}
        override fun get(): Float = color1.b
    }
    private val accessorColorAlpha: FloatPropertyAccessor = object : FloatPropertyAccessor {
        override fun set(value: Float) {color1.a = value}
        override fun get(): Float = color1.a
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

    override fun indexedTypeKey(): IIndexedTypeKey = typeKey
    companion object : EntityComponentType<EShape>() {
        override val typeKey = EntityComponent.createTypeKey(EShape::class.java)
        override fun createEmpty() = EShape()
    }
}