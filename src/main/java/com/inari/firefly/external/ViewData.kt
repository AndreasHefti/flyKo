package com.inari.firefly.external

import com.inari.util.geom.PositionF
import com.inari.commons.geom.Rectangle
import com.inari.commons.graphics.RGBColor
import com.inari.firefly.graphics.BlendMode

interface ViewData {
    val index: Int
    val isBase: Boolean
    val bounds: Rectangle
    val worldPosition: PositionF
    val clearColor: RGBColor
    val tintColor: RGBColor
    var blendMode: BlendMode
    var zoom: Float
    var fboScaler: Float
}