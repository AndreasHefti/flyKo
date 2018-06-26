package com.inari.firefly.external

import com.inari.util.geom.PositionF
import com.inari.commons.geom.Rectangle
import com.inari.commons.graphics.RGBColor
import com.inari.firefly.graphics.BlendMode

abstract class ViewData constructor(
    @JvmField var bounds: Rectangle = Rectangle(0, 0, 0, 0),
    @JvmField var worldPosition: PositionF = PositionF(0, 0),
    @JvmField var clearColor: RGBColor = RGBColor( 0f, 0f, 0f, 1f ),
    @JvmField var tintColor: RGBColor = RGBColor( 1f, 1f, 1f, 1f ),
    @JvmField var blendMode: BlendMode = BlendMode.NONE,
    @JvmField var zoom: Float = 1.0f,
    @JvmField var fboScaler: Float = 4.0f
) {
    abstract val index: Int
    abstract val isBase: Boolean

    override fun toString(): String {
        return "ViewData(baseView=$isBase, " +
            "bounds=$bounds, " +
            "worldPosition=$worldPosition, " +
            "clearColor=$clearColor, " +
            "tintColor=$tintColor, " +
            "blendMode=$blendMode, " +
            "zoom=$zoom, " +
            "fboScaler=$fboScaler)"
    }
}