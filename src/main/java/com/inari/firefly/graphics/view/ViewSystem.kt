package com.inari.firefly.graphics.view

import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.aspect.IAspects
import com.inari.commons.lang.list.DynArray
import com.inari.commons.lang.list.IntBag
import com.inari.firefly.FFContext
import com.inari.firefly.NO_NAME
import com.inari.firefly.component.IComponentMap
import com.inari.firefly.component.MapListener
import com.inari.firefly.external.ViewPortData
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent

object ViewSystem : ComponentSystem {

    override val supportedComponents: IAspects = SystemComponent.ASPECT_GROUP.createAspects(
        View.typeKey,
        Layer.typeKey
    )

    val views: IComponentMap<View>
    val layers: IComponentMap<Layer>

    internal val baseView: View
    internal val activeViewPorts: DynArray<ViewPortData> = DynArray.create(ViewPortData::class.java)
    internal val layersOfView: DynArray<IntBag> = DynArray.create(IntBag::class.java)
    private val orderedView: IntBag = IntBag(10, -1, 5)

    init {
        val viewListener: MapListener<View> = {
                view, action -> when (action) {
                IComponentMap.MapAction.CREATED       -> created(view)
                IComponentMap.MapAction.ACTIVATED     -> activated(view)
                IComponentMap.MapAction.DEACTIVATED   -> deactivated(view)
                IComponentMap.MapAction.DELETED       -> deleted(view)
            }
        }
        val layerListener: MapListener<Layer> = {
                layer, action -> when (action) {
                IComponentMap.MapAction.CREATED       -> created(layer)
//                IComponentMap.MapAction.ACTIVATED     -> activated(layer)
//                IComponentMap.MapAction.DEACTIVATED   -> deactivated(layer)
                IComponentMap.MapAction.DELETED       -> deleted(layer)
                else -> {}
            }
        }

        views = ComponentSystem.createComponentMapping(
            View,
            activationMapping = true,
            nameMapping = true,
            listener = viewListener
        )
        layers = ComponentSystem.createComponentMapping(
            Layer,
            activationMapping = true,
            listener = layerListener
        )

        baseView = View.buildAndGet {
            ff_Name = NO_NAME
            ff_Bounds = Rectangle(0, 0, FFContext.screenWidth, FFContext.screenHeight)
            baseView = true
        }
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
    }

    private fun deactivated(view: View) {
        if (view.isBase)
            throw IllegalStateException("Base View cannot be deactivated")

        updateViewMapping()

        val i = layersOfView.get(view.index()).iterator()
        while (i.hasNext()) {
            layers.deactivate(i.next())
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
        if (!views.exists(layer.ff_ViewIndex))
            throw IllegalStateException("No View exists for Layer: $layer")

        layersOfView
            .get(layer.ff_ViewIndex)
            .add(layer.index())
    }

//    private fun activated(layer: Layer) {}
//
//    private fun deactivated(layer: Layer) {}

    private fun deleted(layer: Layer) {
        layersOfView
            .get(layer.ff_ViewIndex)
            .remove(layer.ff_ViewIndex)
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