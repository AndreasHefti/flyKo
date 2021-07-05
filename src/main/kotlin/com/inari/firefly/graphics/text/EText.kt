package com.inari.firefly.graphics.text

import com.inari.firefly.asset.Asset
import com.inari.firefly.asset.AssetInstanceRefResolver
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.component.ComponentType
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.rendering.Renderer
import com.inari.firefly.graphics.rendering.SimpleTextRenderer
import com.inari.util.graphics.RGBColor

class EText private constructor() : EntityComponent(EText::class.simpleName!!) {

    @JvmField internal var rendererRef = SimpleTextRenderer.instance.index
    @JvmField internal var fontAssetRef = -1
    @JvmField internal var shaderRef = -1

    var renderer = ComponentRefResolver(Renderer) { index-> rendererRef = index }
    var fontAsset = ComponentRefResolver(Asset) { index -> fontAssetRef = index }
    val shader = AssetInstanceRefResolver(
        { index -> shaderRef = index },
        { shaderRef })

    val text: StringBuilder = StringBuilder()
    var tint: RGBColor = RGBColor(1f, 1f, 1f, 1f)
    var blend: BlendMode = BlendMode.NONE

    override fun reset() {
        rendererRef = -1
        fontAssetRef = -1
        shaderRef = -1
        text.setLength(0)
        tint.r = 1f; tint.g = 1f; tint.b = 1f; tint.a = 1f
        blend = BlendMode.NONE
    }

    override fun componentType(): ComponentType<EText> = Companion
    companion object : EntityComponentType<EText>(EText::class) {
        override fun createEmpty() = EText()
    }
}