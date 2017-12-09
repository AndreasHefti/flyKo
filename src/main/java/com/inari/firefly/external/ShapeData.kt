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

    val type: ShapeType
    val vertices: FloatArray
    val segments: Int
    val color1: RGBColor
    val color2: RGBColor
    val color3: RGBColor
    val color4: RGBColor
    val blend: BlendMode
    val fill: Boolean
    val shaderRef: Int
}
