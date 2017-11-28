package com.inari.firefly.external

import com.inari.commons.geom.PositionF
import com.inari.commons.geom.Rectangle
import com.inari.commons.graphics.RGBColor
import com.inari.firefly.graphics.BlendMode

interface ViewPortData {
    val index: Int
    val isBase: Boolean
    val bounds: Rectangle
    val worldPosition: PositionF
    val clearColor: RGBColor
    val tintColor: RGBColor
    val blendMode: BlendMode
    val zoom: Float
    val fboScaler: Float
}