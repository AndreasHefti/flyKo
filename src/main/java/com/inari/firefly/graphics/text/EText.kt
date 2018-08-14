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

class EText private constructor() : EntityComponent() {

    @JvmField internal var rendererRef = SimpleTextRenderer.aspectIndex
    @JvmField internal var fontAssetRef = -1
    @JvmField internal var shaderRef = -1
    @JvmField internal val textBuffer = StringBuffer()
    @JvmField internal val tint = RGBColor(1f, 1f, 1f, 1f)
    @JvmField internal var blend = BlendMode.NONE

    var ff_Renderer = ComponentRefResolver(Renderer) { index-> rendererRef = index }
    var ff_FontAsset = ComponentRefResolver(Asset) { index -> fontAssetRef = index }
    val ff_Shader = AssetInstanceRefResolver(
        { index -> shaderRef = index },
        { shaderRef })

    val ff_TextBuffer: StringBuffer
        get() = textBuffer
    var ff_Tint: RGBColor
        get() = tint
        set(value) { tint(value) }
    var ff_Blend: BlendMode
        get() = blend
        set(value) {blend = value}

    override fun reset() {
        rendererRef = -1
        fontAssetRef = -1
        shaderRef = -1
        textBuffer.setLength(0)
        tint.r = 1f; tint.g = 1f; tint.b = 1f; tint.a = 1f
        blend = BlendMode.NONE
    }

    override fun componentType(): ComponentType<EText> =
        EText.Companion

    companion object : EntityComponentType<EText>(EText::class.java) {
        override fun createEmpty() = EText()
    }
}