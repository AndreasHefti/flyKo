package com.inari.firefly.graphics.view

import com.inari.firefly.component.NamedReference
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent

class Layer private constructor () : SystemComponent() {

    override fun indexedTypeKey() = typeKey

    private val view: NamedReference<View> = NamedReference(View)
    var ff_ViewName: String
        set(value) { view.name = value }
        get() = view.name
    var ff_ViewIndex: Int
        set(value) { view.index = value }
        get() = view.index

    override fun toString(): String {
        return "Layer(view=$view)"
    }

    companion object : SingleType<Layer>() {
        override val typeKey = SystemComponent.createTypeKey(Layer::class.java)
        override fun createEmpty(): Layer = Layer()
    }
}