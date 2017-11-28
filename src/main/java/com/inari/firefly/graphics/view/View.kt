package com.inari.firefly.graphics.view

import com.inari.commons.geom.PositionF
import com.inari.commons.geom.Rectangle
import com.inari.commons.graphics.RGBColor
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.external.ViewPortData

class View private constructor (
    var ff_Bounds: Rectangle,
    var ff_WorldPosition: PositionF,
    var ff_ClearColor: RGBColor,
    var ff_TintColor: RGBColor,
    var ff_BlendMode: BlendMode,
    var ff_Zoom: Float,
    var ff_FboScaler: Float,
    var ff_ControllerId: Int,
    internal var baseView: Boolean = false
) : SystemComponent(), ViewPortData {

    private constructor() : this(
        Rectangle(),
        PositionF(),
        RGBColor( 0f, 0f, 0f, 1f ),
        RGBColor( 1f, 1f, 1f, 1f ),
        BlendMode.NONE,
        1.0f, 4.0f, -1
    )

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

    companion object : SingleType<View>() {
        override val typeKey = SystemComponent.createTypeKey(View::class.java)
        override fun createEmpty(): View = View()
    }
}