package com.inari.firefly.graphics.view

import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentSingleType
import com.inari.util.indexed.Indexer

class Layer private constructor () : SystemComponent() {

    override val indexer: Indexer =
        Indexer(Layer::class.java.name)

    @JvmField internal var viewRef = -1

    val ff_View =
        ComponentRefResolver(View) { index-> viewRef = setIfNotInitialized(index, "ff_View") }

    override fun toString(): String {
        return "Layer(view=$viewRef)"
    }

    override fun componentType(): ComponentType<Layer> =
        Layer.Companion

    companion object : SystemComponentSingleType<Layer>(Layer::class.java) {
        override fun createEmpty() = Layer()
    }
}