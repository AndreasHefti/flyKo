package com.inari.firefly.graphics.view.camera

import com.inari.util.geom.PositionF
import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.NO_CAMERA_PIVOT
import com.inari.firefly.component.CompId
import com.inari.firefly.control.Controller
import com.inari.firefly.graphics.view.View
import com.inari.firefly.graphics.view.ViewChangeEvent
import com.inari.firefly.graphics.view.ViewSystem
import com.inari.firefly.system.component.SubType


class SimpleCamaraController private constructor() : Controller() {

    private var pivot: CameraPivot = NO_CAMERA_PIVOT
    private val snapToBounds = Rectangle()
    private var velocity = 0.25f

    private val pos = PositionF()
    private var view: View? = null

    var ff_Pivot: CameraPivot
        get() = throw UnsupportedOperationException()
        set(value) { pivot = value }
    var ff_SnapToBounds: Rectangle
        get() = snapToBounds
        set(value) {snapToBounds.setFrom(value)}
    var ff_Velocity: Float
        get() = velocity
        set(value) {velocity = value}

    override fun register(id: CompId) {
        view = ViewSystem.views[id]
        adjust()
    }

    override fun unregister(id: CompId) {
        view = null
    }

    fun adjust() {
        val view = this.view ?: return

        if (getPos(view.zoom, view.bounds, view.worldPosition)) {
            view.worldPosition.x = Math.floor(view.worldPosition.x.toDouble() + pos.x).toFloat()
            view.worldPosition.y = Math.floor(view.worldPosition.y.toDouble() + pos.y).toFloat()
            ViewChangeEvent.send(view.componentId, ViewChangeEvent.Type.ORIENTATION)
        }
    }

    override fun update() {
        val view = this.view ?: return

        if (getPos(view.zoom, view.bounds, view.worldPosition)) {
            view.worldPosition.x += pos.x * velocity
            view.worldPosition.y += pos.y * velocity
            ViewChangeEvent.send(view.componentId, ViewChangeEvent.Type.ORIENTATION)
        }
    }

    private fun getPos(zoom: Float, viewBounds: Rectangle, worldPosition: PositionF): Boolean {
        if (pivot === NO_CAMERA_PIVOT)
            return false

        val following = pivot()
        val oneDivZoom = 1f / zoom
        val viewHorizontal = viewBounds.width / oneDivZoom
        val viewHorizontalHalf = viewHorizontal / 2f
        val viewVertical = viewBounds.height / oneDivZoom
        val viewVerticalHalf = viewVertical / 2f

        val xMax = snapToBounds.width - viewHorizontal
        val yMax = snapToBounds.height - viewVertical

        pos.x = following.x + oneDivZoom - viewHorizontalHalf
        pos.y = following.y + oneDivZoom - viewVerticalHalf

        if (pos.x < snapToBounds.x)
            pos.x = snapToBounds.x.toFloat()
        if (pos.y < snapToBounds.y)
            pos.y = snapToBounds.y.toFloat()

        pos.x = Math.min(pos.x, xMax)
        pos.y = Math.min(pos.y, yMax)
        pos.x = Math.ceil(pos.x.toDouble() - worldPosition.x).toFloat()
        pos.y = Math.floor(pos.y.toDouble() - worldPosition.y).toFloat()

        return pos.x != 0f || pos.y != 0f
    }

    companion object : SubType<SimpleCamaraController, Controller>() {
        override val typeKey: IndexedTypeKey = Controller.typeKey
        override fun subType() = SimpleCamaraController::class.java
        override fun createEmpty() = SimpleCamaraController()
    }
}