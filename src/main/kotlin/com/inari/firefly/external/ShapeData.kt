package com.inari.firefly.external

import com.inari.firefly.graphics.BlendMode
import com.inari.util.graphics.RGBColor

enum class ShapeType {
    POINT,
    LINE,
    POLY_LINE,
    POLYGON,
    RECTANGLE,
    CIRCLE,
    ARC,
    CURVE,
    TRIANGLE
}

class ShapeData(
    @JvmField var type: ShapeType = ShapeType.POINT,
    @JvmField var vertices: FloatArray = floatArrayOf(),
    @JvmField var segments: Int = -1,
    @JvmField var color1: RGBColor = RGBColor(1f, 1f, 1f, 1f),
    @JvmField var color2: RGBColor? = null,
    @JvmField var color3: RGBColor? = null,
    @JvmField var color4: RGBColor? = null,
    @JvmField var blend: BlendMode = BlendMode.NONE,
    @JvmField var fill: Boolean = false,
    @JvmField var shaderRef: Int = -1
) {

    fun reset() {
        type = ShapeType.POINT
        vertices = floatArrayOf()
        segments = -1
        color1 = RGBColor(1f, 1f, 1f, 1f)
        color2 = null
        color3 = null
        color4 = null
        blend = BlendMode.NONE
        fill = false
        shaderRef = -1
    }
}


