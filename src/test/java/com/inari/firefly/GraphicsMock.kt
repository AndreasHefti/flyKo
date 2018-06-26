package com.inari.firefly

import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.list.DynArray
import com.inari.commons.lang.list.DynArrayRO
import com.inari.firefly.component.CompId
import com.inari.firefly.external.*
import com.inari.firefly.graphics.view.ViewEvent
import java.util.*

object GraphicsMock : FFGraphics {

    val loadedAssets = DynArray.create(String::class.java, 20, 10)
    val views = ArrayList<CompId>()

    val log = ArrayList<String>()

    override val screenWidth: Int
        get() = 100

    override val screenHeight: Int
        get() = 100

    init {
        FFContext.registerListener(
            ViewEvent,
            object : ViewEvent.Listener {
                override fun invoke(id: CompId, viewPort: ViewData, type: ViewEvent.Type) {
                    when (type) {
                        ViewEvent.Type.VIEW_CREATED -> views.add(id)
                        ViewEvent.Type.VIEW_DELETED -> views.remove(id)
                        else -> {
                        }
                    }
                }
            }
        )
    }

    fun clear() {
        loadedAssets.clear()
        views.clear()
        log.clear()
    }

    override fun createTexture(data: TextureData): Triple<Int, Int, Int> {
        return Triple(loadedAssets.add(data.resourceName), 0, 0)
    }

    override fun disposeTexture(textureId: Int) {
        loadedAssets.remove(textureId)
    }

    override fun createSprite(data: SpriteData): Int {
        return loadedAssets.add("sprite: ${data.textureId} : ${data.x} ${data.y} ${data.width} ${data.height}")
    }

    override fun disposeSprite(spriteId: Int) {
        loadedAssets.remove(spriteId)
    }

    override fun createShader(data: ShaderData): Int {
        return loadedAssets.add(data.name)
    }

    override fun disposeShader(shaderId: Int) {
        loadedAssets.remove(shaderId)
    }



    override fun startRendering(view: ViewData, clear: Boolean) {
        log.add("startRendering::$view")
    }

    override fun renderSprite(renderableSprite: SpriteRenderable, xpos: Float, ypos: Float) {
        log.add("renderSprite::Sprite($renderableSprite)")
    }

    override fun renderSprite(renderableSprite: SpriteRenderable, xpos: Float, ypos: Float, scale: Float) {
        log.add("renderSprite::Sprite($renderableSprite)")
    }

    override fun renderSprite(renderableSprite: SpriteRenderable, transform: TransformData) {
        log.add("renderSprite::Sprite($renderableSprite)")
    }

    override fun renderShape(data: ShapeData) {
        log.add("renderShape:: " + data)
    }

    override fun renderShape(data: ShapeData, transform: TransformData) {
        log.add("renderShape:: $data : $transform")
    }

    override fun endRendering(view: ViewData) {
        log.add("endRendering::$view")
    }

    override fun flush(virtualViews: DynArrayRO<ViewData>) {
        log.add("flush")
    }

    fun loadedAssets(): String {
        return loadedAssets.toString()
    }

    fun views(): String {
        return views.toString()
    }

    fun log(): String {
        return log.toString()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("LowerSystemFacadeMock [loadedAssets=")
        assetsToString(builder)
        builder.append(", views=")
        builder.append(views)
        builder.append(", log=")
        builder.append(log)
        builder.append("]")
        return builder.toString()
    }

    private fun assetsToString(builder: StringBuilder) {
        builder.append("[")
        for (assetName in loadedAssets) {
            builder.append(assetName).append(",")
        }

        if (loadedAssets.size() > 0) {
            builder.deleteCharAt(builder.length - 1)
        }

        builder.append("]")
    }

    override fun getScreenshotPixels(area: Rectangle): ByteArray {
        null!!
    }

}
