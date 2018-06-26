package com.inari.firefly.component

import com.inari.commons.lang.list.DynArray
import com.inari.commons.lang.list.DynArrayRO
import com.inari.firefly.graphics.view.Layer
import com.inari.firefly.graphics.view.View
import com.inari.firefly.graphics.view.ViewLayerAware

class ViewLayerMapping<C : ViewLayerAware> internal constructor(
    private val type: Class<C>
) {

    private val mapping: DynArray<DynArray<C>> =
        DynArray.createTyped(DynArray::class.java)

    operator fun contains(view: Int): Boolean =
        view in mapping

    operator fun contains(view: CompId): Boolean =
        view.index in mapping

    operator fun contains(view: View): Boolean =
        view.index() in mapping

    operator fun contains(viewLayer: ViewLayerAware): Boolean =
        viewLayer.viewIndex in mapping &&
        viewLayer.layerIndex in mapping[viewLayer.viewIndex]

    fun contains(view: Int, layer: Int): Boolean =
        view in mapping &&
            layer in mapping[view]

    operator fun get(view: Int): DynArrayRO<C> {
        if (view !in mapping)
            mapping.set(view, DynArray.create(type))
        return mapping[view]
    }

    operator fun get(view: CompId): DynArrayRO<C> =
        get(view.index)

    operator fun get(view: View): DynArrayRO<C> =
        get(view.index())

    operator fun get(view: Int, layer: Int): C =
        this[view][layer]

    operator fun get(viewLayer: ViewLayerAware): C =
        this[viewLayer.viewIndex][viewLayer.layerIndex]

    operator fun get(view: CompId, layer: CompId): C =
        this[view][layer.index]

    operator fun get(view: View, layer: Layer): C =
        this[view][layer.index()]

    fun add(c: C) {
        internalGet(c.viewIndex).set(c.layerIndex, c)
    }

    fun delete(c: C) {
        internalGet(c.viewIndex).remove(c.layerIndex)
    }

    private fun internalGet(view: Int): DynArray<C> {
        if (view !in mapping)
            mapping.set(view, DynArray.create(type))
        return mapping[view]
    }
}