package com.inari.firefly.external

import com.inari.commons.graphics.RGBColor
import com.inari.commons.lang.list.DynArrayRO
import com.inari.firefly.graphics.BlendMode

interface ShapeData {

    enum class ShapeType {
        POINT,
        LINE,
        POLI_LINE,
        POLIGON,
        RECTANGLE,
        CIRCLE,
        CONE,
        ARC,
        CURVE,
        TRIANGLE
    }

    val shapeType: ShapeType
    val vertices: FloatArray
    val segments: Int
    val colors: DynArrayRO<RGBColor>
    val blendMode: BlendMode
    val isFill: Boolean
    val shaderId: Int
}
