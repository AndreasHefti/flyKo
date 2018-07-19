package com.inari.firefly.graphics.view.camera

import com.inari.util.geom.PositionF
import com.inari.firefly.NO_CAMERA_PIVOT
import com.inari.firefly.component.CompId
import com.inari.firefly.control.Controller
import com.inari.firefly.graphics.view.View
import com.inari.firefly.graphics.view.ViewChangeEvent
import com.inari.firefly.graphics.view.ViewSystem
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.geom.Rectangle


class SimpleCameraController private constructor() : Controller() {

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
        set(value) {snapToBounds(value)}
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

        if (getPos(view.data.zoom, view.data.bounds, view.data.worldPosition)) {
            view.data.worldPosition.x = Math.floor(view.data.worldPosition.x.toDouble() + pos.x).toFloat()
            view.data.worldPosition.y = Math.floor(view.data.worldPosition.y.toDouble() + pos.y).toFloat()
            ViewChangeEvent.send(view.componentId, ViewChangeEvent.Type.ORIENTATION)
        }
    }

    override fun update() {
        val view = this.view ?: return

        if (getPos(view.data.zoom, view.data.bounds, view.data.worldPosition)) {
            view.data.worldPosition.x += pos.x * velocity
            view.data.worldPosition.y += pos.y * velocity
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

        if (pos.x < snapToBounds.pos.x)
            pos.x = snapToBounds.pos.x.toFloat()
        if (pos.y < snapToBounds.pos.y)
            pos.y = snapToBounds.pos.y.toFloat()

        pos.x = Math.min(pos.x, xMax)
        pos.y = Math.min(pos.y, yMax)
        pos.x = Math.ceil(pos.x.toDouble() - worldPosition.x).toFloat()
        pos.y = Math.floor(pos.y.toDouble() - worldPosition.y).toFloat()

        return pos.x != 0f || pos.y != 0f
    }

    companion object : SystemComponentSubType<Controller, SimpleCameraController>(Controller, SimpleCameraController::class.java) {
        override fun createEmpty() = SimpleCameraController()
    }
}