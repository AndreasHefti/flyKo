package com.inari.firefly.graphics.view

import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.aspect.IAspects
import com.inari.commons.lang.list.DynArray
import com.inari.commons.lang.list.IntBag
import com.inari.firefly.FFContext
import com.inari.firefly.NO_NAME
import com.inari.firefly.component.IComponentMap
import com.inari.firefly.component.IComponentMap.MapAction.*
import com.inari.firefly.control.ControllerSystem
import com.inari.firefly.external.ViewPortData
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent.Companion.ASPECT_GROUP

object ViewSystem : ComponentSystem {

    override val supportedComponents: IAspects =
        ASPECT_GROUP.createAspects(View, Layer)

    @JvmField val views = ComponentSystem.createComponentMapping(
        View,
        activationMapping = true,
        nameMapping = true,
        listener = { view, action -> when (action) {
            CREATED       -> created(view)
            ACTIVATED     -> activated(view)
            DEACTIVATED   -> deactivated(view)
            DELETED       -> deleted(view)
        } }
    )
    @JvmField val layers: IComponentMap<Layer> = ComponentSystem.createComponentMapping(
        Layer,
        activationMapping = true,
        listener = { layer, action -> when (action) {
            CREATED       -> created(layer)
//                ACTIVATED     -> activated(layer)
//                DEACTIVATED   -> deactivated(layer)
            DELETED       -> deleted(layer)
            else -> {}
        } }
    )

    @JvmField internal val baseView: View
    @JvmField internal val activeViewPorts: DynArray<ViewPortData> = DynArray.create(ViewPortData::class.java)
    @JvmField internal val layersOfView: DynArray<IntBag> = DynArray.create(IntBag::class.java)

    private val orderedView: IntBag = IntBag(10, -1, 5)

    init {
        baseView = View.buildAndGet {
            ff_Name = NO_NAME
            ff_Bounds = Rectangle(0, 0, FFContext.screenWidth, FFContext.screenHeight)
            baseView = true
        }

        FFContext.loadSystem(this)
    }

    private fun created(view: View) {
        if (view.isBase)
            return

        val index = view.index()
        orderedView.add(index)

        if (layersOfView.contains(index)) {
            layersOfView.set(index, IntBag(10, -1, 5))
        }
    }

    private fun activated(view: View) {
        if (view.isBase)
            return

        updateViewMapping()

        if (view.ff_ControllerId >= 0) {
            ControllerSystem.controller
                .get(view.ff_ControllerId)
                .register(view.componentId)
        }
    }

    private fun deactivated(view: View) {
        if (view.isBase)
            throw IllegalStateException("Base View cannot be deactivated")

        updateViewMapping()

        val i = layersOfView.get(view.index()).iterator()
        while (i.hasNext()) {
            layers.deactivate(i.next())
        }

        if (view.ff_ControllerId >= 0) {
            ControllerSystem.controller
                .get(view.ff_ControllerId)
                .unregister(view.componentId)
        }
    }

    private fun deleted(view: View) {
        if (view.isBase)
            throw IllegalStateException("Base View cannot be deactivated")

        // delete also all layers of this view
        val index = view.index()
        if (layersOfView.contains(index)) {
            val i = layersOfView.get(index).iterator()
            while (i.hasNext()) {
                layers.delete(i.next())
            }
        }

        orderedView.remove(index)
        orderedView.trim()
    }

    private fun created(layer: Layer) {
        if (layer.viewRef in views)
            throw IllegalStateException("No View exists for Layer: $layer")

        layersOfView
            .get(layer.viewRef)
            .add(layer.index())
    }

//    private fun activated(layer: Layer) {}
//
//    private fun deactivated(layer: Layer) {}

    private fun deleted(layer: Layer) {
        layersOfView
            .get(layer.viewRef)
            .remove(layer.viewRef)
    }

    private fun updateViewMapping() {
        activeViewPorts.clear()
        val i = orderedView.iterator()
        while (i.hasNext()) {
            activeViewPorts.add(views.get(i.next()))
        }
    }

    override fun clearSystem() {
        views.clear()
        layers.clear()
    }
}