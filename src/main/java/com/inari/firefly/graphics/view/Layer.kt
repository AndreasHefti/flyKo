package com.inari.firefly.graphics.view

import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent

class Layer private constructor () : SystemComponent() {

    @JvmField internal var viewRef = -1

    val ff_View =
        ComponentRefResolver(View) { index-> viewRef = setIfNotInitialized(index, "ff_View") }

    override fun toString(): String {
        return "Layer(view=$viewRef)"
    }

    override fun componentType(): ComponentType<Layer> =
        Layer.Companion

    companion object : SingleType<Layer>() {
        override val indexedTypeKey by lazy { TypeKeyBuilder.create(Layer::class.java) }
        override fun createEmpty() = Layer()
    }
}