package com.inari.firefly.graphics.shape

import com.inari.commons.graphics.RGBColor
import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.firefly.asset.AssetInstanceRefResolver
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.external.ShapeData
import com.inari.firefly.external.ShapeData.ShapeType
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.setFrom
import java.util.*

class EShape private constructor(): EntityComponent(), ShapeData {

    override var type = ShapeType.POINT
        private set
    override var vertices = floatArrayOf()
        private set
    override val color1 = RGBColor(1f, 1f, 1f, 1f)
    override val color2 = RGBColor(1f, 1f, 1f, 1f)
    override val color3 = RGBColor(1f, 1f, 1f, 1f)
    override val color4 = RGBColor(1f, 1f, 1f, 1f)
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
    var ff_Color1: RGBColor
        get() = color1
        set(value) = color1.setFrom(value)
    var ff_Color2: RGBColor
        get() = color2
        set(value) = color2.setFrom(value)
    var ff_Color3: RGBColor
        get() = color3
        set(value) = color3.setFrom(value)
    var ff_Color4: RGBColor
        get() = color4
        set(value) = color4.setFrom(value)
    var ff_Segments: Int
        get() = segments
        set(value) { segments = value }
    var ff_Fill: Boolean
        get() = fill
        set(value) { fill = value }
    var ff_Blend: BlendMode
        get() = blend
        set(value) { blend = value }
    val ff_Shader =
        AssetInstanceRefResolver({ index -> shaderRef = setIfNotInitialized(index, "ff_Shader") })

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

    override fun indexedTypeKey(): IIndexedTypeKey = typeKey
    companion object : EntityComponentType<EShape>() {
        override val typeKey = EntityComponent.createTypeKey(EShape::class.java)
        override fun createEmpty() = EShape()
    }
}