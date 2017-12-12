package com.inari.firefly.graphics.view

import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.aspect.IAspects
import com.inari.commons.lang.list.DynArray
import com.inari.commons.lang.list.IntBag
import com.inari.firefly.FFContext
import com.inari.firefly.NO_NAME
import com.inari.firefly.component.ComponentMap
import com.inari.firefly.component.ComponentMap.MapAction.*
import com.inari.firefly.control.ControllerSystem
import com.inari.firefly.external.ViewData
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent.Companion.SYSTEM_COMPONENT_ASPECTS

object ViewSystem : ComponentSystem {

    override val supportedComponents: IAspects =
        SYSTEM_COMPONENT_ASPECTS.createAspects(View, Layer)

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
    @JvmField val layers: ComponentMap<Layer> = ComponentSystem.createComponentMapping(
        Layer,
        activationMapping = true,
        listener = { layer, action -> when (action) {
            CREATED       -> created(layer)
            DELETED       -> deleted(layer)
            else -> {}
        } }
    )

    @JvmField val baseView: View
    @JvmField internal val activeViewPorts: DynArray<ViewData> = DynArray.create(ViewData::class.java)
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
        val index = view.index()
        if (index !in layersOfView)
            layersOfView.set(index, IntBag(10, -1, 5))

        if (!view.isBase)
            orderedView.add(index)

        ViewEvent.send(view.componentId, view, ViewEvent.Type.VIEW_CREATED)
    }

    private fun activated(view: View) {
        if (view.isBase)
            return

        updateViewMapping()

        if (view.controllerRef >= 0)
            ControllerSystem.controller[view.controllerRef]
                .register(view.componentId)

        ViewEvent.send(view.componentId, view, ViewEvent.Type.VIEW_ACTIVATED)
    }

    private fun deactivated(view: View) {
        if (view.isBase)
            throw IllegalStateException("Base View cannot be deactivated")

        updateViewMapping()

        val i = layersOfView.get(view.index()).iterator()
        while (i.hasNext())
            layers.deactivate(i.next())

        if (view.controllerRef >= 0)
            ControllerSystem.controller[view.controllerRef]
                .unregister(view.componentId)

        ViewEvent.send(view.componentId, view, ViewEvent.Type.VIEW_DISPOSED)
    }

    private fun deleted(view: View) {
        if (view.isBase)
            throw IllegalStateException("Base View cannot be deactivated")

        // delete also all layers of this view
        val index = view.index()
        if (layersOfView.contains(index)) {
            val i = layersOfView.get(index).iterator()
            while (i.hasNext())
                layers.delete(i.next())
        }

        orderedView.remove(index)
        orderedView.trim()

        ViewEvent.send(view.componentId, view, ViewEvent.Type.VIEW_DELETED)
    }

    private fun created(layer: Layer) {
        if (layer.viewRef in views)
            throw IllegalStateException("No View exists for Layer: $layer")

        layersOfView
            .get(layer.viewRef)
            .add(layer.index())
    }

    private fun deleted(layer: Layer) {
        layersOfView
            .get(layer.viewRef)
            .remove(layer.viewRef)
    }

    private fun updateViewMapping() {
        activeViewPorts.clear()
        val i = orderedView.iterator()
        while (i.hasNext()) {
            val nextId = i.next()
            if (views.isActive(nextId)) {
                activeViewPorts.add(views[nextId])
            }
        }
    }

    override fun clearSystem() {
        var i = 0
        while (i < views.map.capacity()) {
            val view = views.map[i++] ?: continue
            if (view.isBase)
                continue

            views.delete(view.index)
        }
        layers.clear()
    }
}