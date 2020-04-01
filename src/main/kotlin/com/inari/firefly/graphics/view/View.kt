package com.inari.firefly.graphics.view

import com.inari.firefly.component.CompId
import com.inari.util.geom.PositionF
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.Controller
import com.inari.firefly.control.SingleComponentController
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.external.ViewData
import com.inari.firefly.system.component.SystemComponentBuilder
import com.inari.firefly.system.component.SystemComponentSingleType
import com.inari.util.geom.Rectangle
import com.inari.util.graphics.RGBColor

class View private constructor (
    @JvmField internal var baseView: Boolean = false
) : SystemComponent(View::class.java.name) {

    @JvmField internal var controllerRef = -1
    @JvmField internal val data = object : ViewData() {
        override val index: Int
            get() = super@View.index
        override val isBase: Boolean
            get() = baseView
    }

    var ff_Bounds: Rectangle
        get() = data.bounds
        set(value) { data.bounds(value) }
    var ff_WorldPosition: PositionF
        get() = data.worldPosition
        set(value) { data.worldPosition(value) }
    var ff_ClearColor: RGBColor
        get() = data.clearColor
        set(value) { data.clearColor(value) }
    var ff_TintColor: RGBColor
        get() = data.tintColor
        set(value) { data.tintColor(value) }
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
        ComponentRefResolver(Controller) { index-> controllerRef = setIfNotInitialized(index, "controllerRef") }
    fun <C : Controller> ff_WithController(builder: SystemComponentBuilder<C>, configure: (C.() -> Unit)): CompId {
        val id = builder. build(configure)
        controllerRef = id.index
        return id
    }
    fun <C : Controller> ff_WithActiveController(builder: SystemComponentBuilder<C>, configure: (C.() -> Unit)): CompId {
        val id = builder.buildAndActivate(configure)
        controllerRef = id.index
        return id
    }

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

    override fun componentType() = Companion
    companion object : SystemComponentSingleType<View>(View::class.java) {
        override fun createEmpty() = View()
    }
}