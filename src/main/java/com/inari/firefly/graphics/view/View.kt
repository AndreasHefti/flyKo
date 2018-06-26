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
) : SystemComponent() {

    @JvmField internal var controllerRef = -1
    @JvmField internal val data = object : ViewData() {
        override val index: Int
            get() = index()
        override val isBase: Boolean
            get() = baseView
    }

    var ff_Bounds: Rectangle
        get() = data.bounds
        set(value) { data.bounds.setFrom(value) }
    var ff_WorldPosition: PositionF
        get() = data.worldPosition
        set(value) { data.worldPosition.setFrom(value) }
    var ff_ClearColor: RGBColor
        get() = data.clearColor
        set(value) { data.clearColor.setFrom(value) }
    var ff_TintColor: RGBColor
        get() = data.tintColor
        set(value) { data.tintColor.setFrom(value) }
    var ff_BlendMode: BlendMode
        get() = data.blendMode
        set(value) { data.blendMode = value }
    var ff_Zoom: Float
        get() = data.zoom
        set(value) { data.zoom = value }
    var ff_FboScaler: Float
        get() = data.fboScaler
        set(value) { data.fboScaler = value }
    var ff_Controller =
        ComponentRefResolver(Controller, { index-> controllerRef = setIfNotInitialized(index, "ff_ControllerId") })

    override fun indexedTypeKey() = typeKey
    override fun toString(): String {
        return "View(baseView=$baseView, " +
            "controllerRef=$controllerRef, " +
            "bounds=${data.bounds}, " +
            "worldPosition=${data.worldPosition}, " +
            "clearColor=${data.clearColor}, " +
            "tintColor=${data.tintColor}, " +
            "blendMode=${data.blendMode}, " +
            "zoom=${data.zoom}, " +
            "fboScaler=${data.fboScaler})"
    }

    companion object : SingleType<View>() {
        override val typeKey = SystemComponent.createTypeKey(View::class.java)
        override fun createEmpty() = View()
    }
}