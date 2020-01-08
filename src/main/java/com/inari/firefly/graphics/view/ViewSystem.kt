package com.inari.firefly.graphics.view

import com.inari.firefly.BASE_VIEW
import com.inari.firefly.FFContext
import com.inari.firefly.component.ComponentMap
import com.inari.firefly.component.ComponentMap.MapAction.*
import com.inari.firefly.control.ControllerSystem
import com.inari.firefly.external.ViewData
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.aspect.Aspects
import com.inari.util.collection.DynArray
import com.inari.util.collection.DynIntArray

object ViewSystem : ComponentSystem {

    override val supportedComponents: Aspects =
        SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(View, Layer)

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
    @JvmField internal val activeViewPorts: DynArray<ViewData> = DynArray.of(ViewData::class.java)
    @JvmField internal val layersOfView: DynArray<DynIntArray> = DynArray.of(DynIntArray::class.java)

    private val orderedView: DynIntArray = DynIntArray(10, -1, 5)

    init {
        baseView = View.buildAndGet {
            ff_Name = BASE_VIEW
            ff_Bounds(w=FFContext.screenWidth, h=FFContext.screenHeight)
            baseView = true
        }

        FFContext.loadSystem(this)
    }

    private fun created(view: View) {
        val index = view.index
        if (index !in layersOfView)
            layersOfView[index] = DynIntArray(10, -1, 5)

        if (!view.baseView)
            orderedView.add(index)

        ViewEvent.send(view.componentId, view.data, ViewEvent.Type.VIEW_CREATED)
    }

    private fun activated(view: View) {
        if (view.baseView)
            return

        updateViewMapping()

        if (view.controllerRef >= 0)
            ControllerSystem.controller[view.controllerRef]
                .register(view.componentId)

        ViewEvent.send(view.componentId, view.data, ViewEvent.Type.VIEW_ACTIVATED)
    }

    private fun deactivated(view: View) {
        if (view.baseView)
            throw IllegalStateException("Base View cannot be deactivated")

        updateViewMapping()
        if (view.controllerRef >= 0)
            ControllerSystem.controller[view.controllerRef]
                .unregister(view.componentId)

        ViewEvent.send(view.componentId, view.data, ViewEvent.Type.VIEW_DISPOSED)
    }

    private fun deleted(view: View) {
        if (view.baseView)
            throw IllegalStateException("Base View cannot be deactivated")

        // delete also all layers of this view
        val index = view.index
        layersOfView[index]?.also { bag -> run {
            val i = bag.iterator()
            while (i.hasNext())
                layers.delete(i.next())
        } }

        orderedView.remove(index)
        orderedView.trim()

        ViewEvent.send(view.componentId, view.data, ViewEvent.Type.VIEW_DELETED)
    }

    private fun created(layer: Layer) {
        if (layer.viewRef !in views)
            throw IllegalStateException("No View exists for Layer: $layer")
        layersOfView[layer.viewRef]?.add(layer.index)
    }

    private fun deleted(layer: Layer) =
        layersOfView[layer.viewRef]?.remove(layer.viewRef)


    private fun updateViewMapping() {
        activeViewPorts.clear()
        val i = orderedView.iterator()
        while (i.hasNext()) {
            val nextId = i.next()
            if (views.isActive(nextId)) {
                activeViewPorts.add(views[nextId].data)
            }
        }
    }

    override fun clearSystem() {
        var i = 0
        while (i < views.map.capacity) {
            val view = views.map[i++] ?: continue
            if (view.baseView)
                continue

            views.delete(view.index)
        }
        layers.clear()
    }
}