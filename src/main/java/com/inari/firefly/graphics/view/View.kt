package com.inari.firefly.graphics.view

import com.inari.commons.geom.PositionF
import com.inari.commons.geom.Rectangle
import com.inari.commons.graphics.RGBColor
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.external.ViewPortData

class View private constructor (
    // TODO
    var ff_Bounds: Rectangle = Rectangle(),
    var ff_WorldPosition: PositionF = PositionF(),
    var ff_ClearColor: RGBColor = RGBColor( 0f, 0f, 0f, 1f ),
    var ff_TintColor: RGBColor = RGBColor( 1f, 1f, 1f, 1f ),
    var ff_BlendMode: BlendMode = BlendMode.NONE,
    var ff_Zoom: Float = 1.0f,
    var ff_FboScaler: Float = 4.0f,
    var ff_ControllerId: Int = -1,
    internal var baseView: Boolean = false
) : SystemComponent(), ViewPortData {

    override fun indexedTypeKey() = typeKey

    override val index: Int
        get() = index()
    override val isBase: Boolean
        get() = baseView
    override val bounds: Rectangle
        get() = ff_Bounds
    override val worldPosition: PositionF
        get() = ff_WorldPosition
    override val clearColor: RGBColor
        get() = ff_ClearColor
    override val tintColor: RGBColor
        get() = ff_TintColor
    override val blendMode: BlendMode
        get() = ff_BlendMode
    override val zoom: Float
        get() = ff_Zoom
    override val fboScaler: Float
        get() = ff_FboScaler

    override fun toString(): String {
        return "View(ff_Bounds=$ff_Bounds, " +
            "ff_WorldPosition=$ff_WorldPosition, " +
            "ff_ClearColor=$ff_ClearColor, " +
            "ff_TintColor=$ff_TintColor, " +
            "ff_BlendMode=$ff_BlendMode, " +
            "ff_Zoom=$ff_Zoom, " +
            "ff_FboScaler=$ff_FboScaler, " +
            "ff_ControllerId=$ff_ControllerId, " +
            "baseView=$baseView)"
    }

    companion object : SingleType<View>() {
        override val typeKey = SystemComponent.createTypeKey(View::class.java)
        override fun createEmpty() = View()
    }
}