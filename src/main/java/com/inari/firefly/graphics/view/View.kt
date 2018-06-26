package com.inari.firefly.graphics.view

import com.inari.util.geom.PositionF
import com.inari.commons.geom.Rectangle
import com.inari.commons.graphics.RGBColor
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.Controller
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.external.ViewData
import com.inari.firefly.setFrom

class View private constructor (
    @JvmField internal var baseView: Boolean = false
) : SystemComponent(), ViewData {

    @JvmField internal var controllerRef = -1

    override val index: Int get() = index()
    override val isBase: Boolean get() = baseView
    override val bounds = Rectangle()
    override val worldPosition = PositionF()
    override val clearColor = RGBColor( 0f, 0f, 0f, 1f )
    override val tintColor = RGBColor( 1f, 1f, 1f, 1f )
    override var blendMode = BlendMode.NONE
    override var zoom = 1.0f
    override var fboScaler = 4.0f

    var ff_Bounds: Rectangle
        get() = bounds
        set(value) { bounds.setFrom(value) }
    var ff_WorldPosition: PositionF
        get() = worldPosition
        set(value) { worldPosition.setFrom(value) }
    var ff_ClearColor: RGBColor
        get() = clearColor
        set(value) { clearColor.setFrom(value) }
    var ff_TintColor: RGBColor
        get() = tintColor
        set(value) { tintColor.setFrom(value) }
    var ff_BlendMode: BlendMode
        get() = blendMode
        set(value) { blendMode = value }
    var ff_Zoom: Float
        get() = zoom
        set(value) { zoom = value }
    var ff_FboScaler: Float
        get() = fboScaler
        set(value) { fboScaler = value }
    var ff_Controller =
        ComponentRefResolver(Controller, { index-> controllerRef = setIfNotInitialized(index, "ff_ControllerId") })

    override fun indexedTypeKey() = typeKey
    override fun toString(): String {
        return "View(baseView=$baseView, " +
            "controllerRef=$controllerRef, " +
            "bounds=$bounds, " +
            "worldPosition=$worldPosition, " +
            "clearColor=$clearColor, " +
            "tintColor=$tintColor, " +
            "blendMode=$blendMode, " +
            "zoom=$zoom, " +
            "fboScaler=$fboScaler)"
    }

    companion object : SingleType<View>() {
        override val typeKey = SystemComponent.createTypeKey(View::class.java)
        override fun createEmpty() = View()
    }
}