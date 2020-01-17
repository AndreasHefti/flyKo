package com.inari.firefly.graphics.tile.set

import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.view.Layer
import com.inari.firefly.graphics.view.View
import com.inari.util.collection.DynArray
import com.inari.util.graphics.RGBColor
import java.util.*

@ComponentDSL
class TileSetActivation internal constructor() {

    @JvmField internal var viewRef = -1
    @JvmField internal val layers = BitSet()
    @JvmField internal val tintColors: DynArray<RGBColor> =
            DynArray.of(RGBColor::class.java, 5, 5)
    @JvmField internal val blendModes: DynArray<BlendMode> =
            DynArray.of(BlendMode::class.java, 5, 5)

    val ff_View = ComponentRefResolver(View) { index-> viewRef = index }

    val ff_withLayer: (LayerDefaults.() -> Unit) -> Unit = { configure ->
        val layerDefaults = LayerDefaults()
        layerDefaults.also(configure)
        layers[layerDefaults.layerRef] = true
        if (layerDefaults.tintColor != null)
            tintColors[layerDefaults.layerRef] = layerDefaults.tintColor
        if (layerDefaults.blendMode != null)
            blendModes[layerDefaults.layerRef] = layerDefaults.blendMode
    }

    operator fun contains(layer: Layer) = layers.get(layer.index)

    companion object {

        val of: (TileSetActivation.() -> Unit) -> TileSetActivation = { configure ->
            val comp = TileSetActivation()
            comp.also(configure)
            comp
        }

    }

    @ComponentDSL
    class LayerDefaults internal constructor() {
        @JvmField internal var layerRef = -1
        val ff_Layer = ComponentRefResolver(Layer) { index-> layerRef = index }

        @JvmField internal var tintColor: RGBColor? = null
        var ff_TintColor: RGBColor
            get() = tintColor!!
            set(value) {tintColor = value}

        @JvmField internal var blendMode: BlendMode? = null
        var ff_BlendMode: BlendMode
            get() = blendMode!!
            set(value) {blendMode = value}
    }

}