package com.inari.firefly.system

import com.inari.commons.graphics.RGBColor
import com.inari.commons.lang.list.DynArray
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.SYSTEM_FONT
import com.inari.firefly.asset.AssetSystem
import com.inari.firefly.external.ShapeData
import com.inari.firefly.external.SpriteRenderable
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.text.FontAsset
import com.inari.firefly.graphics.view.ViewSystem


object FFInfoSystem : FFSystem {

    private var active = false
    private val infos = DynArray.create(SysInfo::class.java)
    private val buffer = StringBuffer()

    private var width = 0
    private var hStep = 0
    private var vStep = 0

    private lateinit var font: FontAsset
    private val graphics = FFContext.graphics

    private val renderListener = object : FFApp.PostRenderEvent.Listener{
        override fun invoke() {
            graphics.startRendering( ViewSystem.baseView, false )
            renderSystemInfoDisplay()
            graphics.endRendering( ViewSystem.baseView )
            graphics.flush(FFApp.NO_VIRTUAL_VIEW_PORTS)
        }
    }

    init {
        if (SYSTEM_FONT !in AssetSystem.assets)
            throw ExceptionInInitializerError("No FontAsset for SYSTEM_FONT found in AssetSystem. SYSTEM_FONT must be defined")
        font = AssetSystem.assets.getAs(SYSTEM_FONT)
    }

    fun activate() {
        if (active)
            return
        FFContext.registerListener(FFApp.PostRenderEvent, renderListener)
        active = true
    }

    fun deactivate() {
        if (!active)
            return
        FFContext.disposeListener(FFApp.PostRenderEvent, renderListener)
        active = false
    }

    private fun renderSystemInfoDisplay() {
        update()

        infoDisplayBackground.rectVertices[2] = (width * hStep + hStep).toFloat()
        infoDisplayBackground.rectVertices[3] = (infos.size() * vStep + vStep).toFloat()
        graphics.renderShape(infoDisplayBackground)

        var xpos = 5f
        var ypos = 5f

        for (i in 0 until buffer.length) {
            val character = buffer[i]
            if (character == '\n') {
                xpos = 0f
                ypos += vStep
                continue
            }

            textRenderable.sprId = font[character]
            graphics.renderSprite(textRenderable, xpos, ypos)
            xpos += hStep
        }
    }

    fun update() {
        var startIndex = 0
        var i = 0
        while (i < infos.capacity()) {
            val info = infos.get(i++) ?: continue

            info.update(buffer, startIndex)
            startIndex += info.length + 1
        }
    }

    override fun clearSystem() {
        TODO("not implemented")
    }

    interface SysInfo {
        val name: String
        val length: Int
        fun update(buffer: StringBuffer, bufferStartPointer: Int)
    }

    private val textRenderable = object : SpriteRenderable {

        @JvmField internal var sprId = -1
        @JvmField internal val tint = RGBColor(1f, 1f, 1f, 1f)

        override val spriteId: Int get() = sprId
        override val tintColor: RGBColor = tint
        override val blendMode: BlendMode =  BlendMode.NORMAL_ALPHA
        override val shaderId: Int = -1
    }

    private val infoDisplayBackground = object : ShapeData {

        @JvmField internal val color = RGBColor(1f, 1f, 1f, 1f)
         @JvmField internal val rectVertices: FloatArray = floatArrayOf(0f, 0f, 0f, 0f)

        override val type: ShapeData.ShapeType = ShapeData.ShapeType.RECTANGLE
        override val vertices: FloatArray = rectVertices
        override val segments: Int = 0
        override val color1: RGBColor = color
        override val color2: RGBColor = color
        override val color3: RGBColor = color
        override val color4: RGBColor = color
        override val blend: BlendMode = BlendMode.NORMAL_ALPHA
        override val fill: Boolean = true
        override val shaderRef: Int = -1
    }

}