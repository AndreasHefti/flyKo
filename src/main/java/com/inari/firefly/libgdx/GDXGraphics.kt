package com.inari.firefly.libgdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.Texture.TextureWrap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.inari.firefly.FFContext
import com.inari.firefly.NO_PROGRAM
import com.inari.firefly.NULL_INT_FUNCTION
import com.inari.firefly.component.CompId
import com.inari.firefly.external.*
import com.inari.firefly.external.ShapeType.*
import com.inari.firefly.external.TextureData
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.view.ViewEvent
import com.inari.firefly.libgdx.filter.ColorFilteredTextureData
import com.inari.util.collection.DynArray
import com.inari.util.collection.DynArrayRO
import com.inari.util.geom.GeomUtils
import com.inari.util.geom.Rectangle
import com.inari.util.graphics.RGBColor
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import java.nio.ByteBuffer
import java.nio.ByteOrder


object GDXGraphics : FFGraphics {

    private val textures: DynArray<Texture> = DynArray.of(100, 50)
    private val sprites: DynArray<TextureRegion> = DynArray.of(200, 300)
    private val viewports: DynArray<ViewportData> = DynArray.of(20, 10)
    private val shaders: DynArray<ShaderProgram> = DynArray.of(20, 10)

    private val spriteBatch = SpriteBatch()
    private var shapeRenderer = ShapeRenderer()

    private var baseViewport: ViewportData? = null
    private var baseView: ViewData? = null

    private var activeViewport: ViewportData? = null
    private var activeShaderId = -1
    private var activeShapeShaderId = -1
    private var activeBlend = BlendMode.NONE

    override val screenHeight: Int
        get() = Gdx.graphics.height
    override val screenWidth: Int
        get() = Gdx.graphics.width

    init {
        FFContext.registerListener(ViewEvent, object : ViewEvent.Listener {
            override fun invoke(id: CompId, viewPort: ViewData, type: ViewEvent.Type) {
                when (type) {
                    ViewEvent.Type.VIEW_CREATED -> {
                        val viewport: ViewportData
                        if (!viewPort.isBase) {
                            viewport = createVirtualViewport(viewPort)
                        } else {
                            viewport = createBaseViewport(viewPort)
                            baseViewport = viewport
                            baseView = viewPort
                        }
                        viewports[id.instanceId] = viewport
                    }
                    ViewEvent.Type.VIEW_DELETED -> {
                        viewports.remove(id.instanceId)?.dispose()
                    }
                    else -> {}
                }
            }
        })
    }

    override fun createTexture(data: TextureData): Triple<Int, Int, Int> {
        val colorConverter = data.colorConverter

        val texture = if (colorConverter != NULL_INT_FUNCTION)
            Texture(ColorFilteredTextureData(data.resourceName, colorConverter))
        else
            Texture( Gdx.files.internal(data.resourceName), data.isMipmap )

        val textureId = textures.add(texture)

        if ( data.wrapS >= 0 )
            texture.setWrap(getLibGDXTextureWrap(data.wrapS), texture.vWrap)
        if ( data.wrapT >= 0 )
            texture.setWrap(texture.uWrap, getLibGDXTextureWrap(data.wrapT))
        if ( data.minFilter >= 0 )
            texture.setFilter(getLibGDXTextureFilter( data.minFilter), texture.magFilter)
        if ( data.magFilter >= 0 )
            texture.setFilter(texture.minFilter, getLibGDXTextureFilter(data.magFilter))

        return Triple(textureId, texture.width, texture.height)
    }

    override fun disposeTexture(textureId: Int) {
        textures.remove(textureId)?.dispose()
    }

    override fun createSprite(data: SpriteData): Int {
        if (data.textureId !in textures)
            throw IllegalStateException("Texture with id: ${data.textureId} not loaded" )

        val texture = textures[data.textureId]
        val sprite = TextureRegion(
            texture,
            data.region.x,
            data.region.y,
            data.region.width,
            data.region.height
        )

        sprite.flip(data.isVerticalFlip, !data.isHorizontalFlip)
        return sprites.add(sprite)
    }

    override fun disposeSprite(spriteId: Int) {
        sprites.remove(spriteId)
    }

    override fun createShader(data: ShaderData): Int {
        var vertexShader = data.vertexShaderProgram
        var fragmentShader = data.fragmentShaderProgram

        if (vertexShader === NO_PROGRAM) {
            try {
                vertexShader = Gdx.files.internal(data.vertexShaderResourceName).readString()
            } catch (e: Exception) {
                throw RuntimeException("Failed to load vertex shader from resource: " + data.vertexShaderResourceName, e)
            }
        }

        if (fragmentShader === NO_PROGRAM) {
            try {
                fragmentShader = Gdx.files.internal(data.fragmentShaderResourceName).readString()
            } catch (e: Exception) {
                throw RuntimeException("Failed to load fragment shader from resource: " + data.fragmentShaderResourceName, e)
            }

        }

        val shaderProgram = ShaderProgram(vertexShader, fragmentShader)
        if (shaderProgram.isCompiled) {
            val compileLog = shaderProgram.log
            println("Shader Compiled: $compileLog")
        } else {
            throw RuntimeException("ShaderData with name: " + data.name + " failed to compile:" + shaderProgram.log)
        }

        return shaders.add(shaderProgram)
    }

    override fun disposeShader(shaderId: Int) {
        shaders.remove( shaderId )?.dispose()
    }

    override fun startRendering(view: ViewData, clear: Boolean) {
        activeViewport = viewports[view.index]
        activeViewport?.activate(spriteBatch, shapeRenderer, view, clear)
        spriteBatch.begin()
    }

    override fun renderSprite(renderableSprite: SpriteRenderable, xpos: Float, ypos: Float) {
        setColorAndBlendMode(renderableSprite.tintColor, renderableSprite.blendMode)
        val sprite = sprites[renderableSprite.spriteId]
        setShaderForSpriteBatch(renderableSprite)

        spriteBatch.draw(sprite, xpos, ypos)
    }

    override fun renderSprite(renderableSprite: SpriteRenderable, xpos: Float, ypos: Float, scale: Float) {
        val sprite = sprites[renderableSprite.spriteId] ?: return
        setColorAndBlendMode(renderableSprite.tintColor, renderableSprite.blendMode)
        setShaderForSpriteBatch(renderableSprite)
        spriteBatch.draw(
            sprite, xpos, ypos, 0F, 0F,
            sprite.regionWidth.toFloat(),
            sprite.regionHeight.toFloat(),
            scale, scale, 0F
        )
    }

    override fun renderSprite(renderableSprite: SpriteRenderable, transform: TransformData) {
        val sprite = sprites[renderableSprite.spriteId] ?: return
        setColorAndBlendMode(renderableSprite.tintColor, renderableSprite.blendMode)
        setShaderForSpriteBatch(renderableSprite)
        spriteBatch.draw(
            sprite,
            transform.position.x,
            transform.position.y,
            transform.pivot.x,
            transform.pivot.y,
            sprite.regionWidth.toFloat(),
            sprite.regionHeight.toFloat(),
            transform.scale.dx,
            transform.scale.dy,
            transform.rotation
        )
    }

    override fun renderShape(data: ShapeData) {
        val shaderId = data.shaderRef
        if (shaderId != activeShapeShaderId)
            shapeRenderer = if (shaderId < 0)
                ShapeRenderer()
            else
                ShapeRenderer(1000, shaders[shaderId])

        getShapeColor(data.color1, SHAPE_COLOR_1)
        getShapeColor(data.color2 ?: data.color1, SHAPE_COLOR_2)
        getShapeColor(data.color3 ?: data.color1, SHAPE_COLOR_3)
        getShapeColor(data.color4 ?: data.color1, SHAPE_COLOR_4)
        shapeRenderer.color = SHAPE_COLOR_1

        val type = data.type
        val shapeType = when {
            type === POINT  -> ShapeRenderer.ShapeType.Point
            data.fill                           -> ShapeRenderer.ShapeType.Filled
            else                                -> ShapeRenderer.ShapeType.Line
        }

        var restartSpriteBatch = false
        if (spriteBatch.isDrawing) {
            restartSpriteBatch = true
            spriteBatch.end()
        }
        shapeRenderer.begin(shapeType)

        val blendMode = data.blend
        val vertices = data.vertices
        val segments = data.segments
        if (blendMode !== BlendMode.NONE) {
            Gdx.gl.glEnable(GL20.GL_BLEND)
            Gdx.gl.glBlendColor(1f, 1f, 1f, 1f)
            Gdx.gl.glBlendFunc(blendMode.source, blendMode.dest)
        }
        var index = 0
        when (type) {
            POINT       -> while (index < vertices.size) shapeRenderer.point(vertices[index++], vertices[index++], 0f)
            LINE        -> while (index < vertices.size) shapeRenderer.line(vertices[index++], vertices[index++], vertices[index++], vertices[index++], SHAPE_COLOR_1, SHAPE_COLOR_2)
            POLY_LINE   -> shapeRenderer.polyline(vertices)
            POLYGON     -> shapeRenderer.polygon(vertices)
            RECTANGLE   -> while (index < vertices.size) shapeRenderer.rect(vertices[index++], vertices[index++], vertices[index++], vertices[index++], SHAPE_COLOR_1, SHAPE_COLOR_2, SHAPE_COLOR_3, SHAPE_COLOR_4)
            CIRCLE      -> while (index < vertices.size) shapeRenderer.circle(vertices[index++], vertices[index++], vertices[index++], segments)
            CONE        -> while (index < vertices.size) shapeRenderer.cone(vertices[index++], vertices[index++], vertices[index++], vertices[index++], vertices[index++], segments)
            ARC         -> while (index < vertices.size) shapeRenderer.arc(vertices[index++], vertices[index++], vertices[index++], vertices[index++], vertices[index++], segments)
            CURVE       -> while (index < vertices.size) shapeRenderer.curve(
                            vertices[index++], vertices[index++], vertices[index++], vertices[index++],
                            vertices[index++], vertices[index++], vertices[index++], vertices[index++], segments
                        )
            TRIANGLE    -> while (index < vertices.size) shapeRenderer.triangle(
                            vertices[index++], vertices[index++], vertices[index++],
                            vertices[index++], vertices[index++], vertices[index++], SHAPE_COLOR_1, SHAPE_COLOR_2, SHAPE_COLOR_3
                        )
        }

        shapeRenderer.flush()
        shapeRenderer.end()

        if (restartSpriteBatch)
            spriteBatch.begin()

        Gdx.gl.glDisable(GL20.GL_BLEND)
    }

    override fun renderShape(data: ShapeData, transform: TransformData) {
        shapeRenderer.identity()
        shapeRenderer.translate(transform.position.x, transform.position.y, 0f)

        if (transform.hasScale) {
            shapeRenderer.translate(transform.pivot.x, transform.pivot.y, 0f)
            shapeRenderer.scale(transform.scale.dx, transform.scale.dy, 0f)
            shapeRenderer.translate(-transform.pivot.x, -transform.pivot.y, 0f)
        }

        if (transform.hasRotation) {
            shapeRenderer.translate(transform.pivot.x, transform.pivot.y, 0f)
            shapeRenderer.rotate(0f, 0f, 1f, transform.rotation)
            shapeRenderer.translate(-transform.pivot.x, -transform.pivot.y, 0f)
        }

        renderShape(data)
        shapeRenderer.identity()
    }

    override fun endRendering(view: ViewData) {
        spriteBatch.flush()
        if (!view.isBase )
            activeViewport?.fbo?.end()
        spriteBatch.end()
        activeViewport = null
    }

    override fun flush(virtualViews: DynArrayRO<ViewData>) {
        if (!virtualViews.isEmpty) {
            baseViewport?.activate(spriteBatch, shapeRenderer, baseView!!, true)
            spriteBatch.begin()

            var i = 0
            while (i < virtualViews.capacity) {
                val virtualView = virtualViews[i++] ?: continue
                val viewport = viewports[virtualView.index] ?: continue
                val bounds = virtualView.bounds
                setColorAndBlendMode(virtualView.tintColor, virtualView.blendMode)

                spriteBatch.draw(
                    viewport.fboTexture,
                    bounds.pos.x.toFloat(), bounds.pos.y.toFloat(),
                    bounds.width.toFloat(), bounds.height.toFloat()
                )
            }
            spriteBatch.end()
        }

        spriteBatch.flush()
        activeBlend = BlendMode.NONE
    }


    override fun getScreenshotPixels(area: Rectangle): ByteArray {
        val flippedY = screenHeight - area.height + area.pos.y
        val size = area.width * area.height * 3
        val screenContents = ByteBuffer.allocateDirect(size).order(ByteOrder.LITTLE_ENDIAN)
        GL11.glReadPixels(area.pos.x, flippedY, area.width, area.height, GL12.GL_BGR, GL11.GL_UNSIGNED_BYTE, screenContents)
        val array = ByteArray(size)
        val inverseArray = ByteArray(size)
        screenContents.get(array)

        var y = 0
        while (y < area.height) {
            System.arraycopy(
                array,
                GeomUtils.getFlatArrayIndex(0, area.height - y - 1, area.width * 3),
                inverseArray,
                GeomUtils.getFlatArrayIndex(0, y, area.width * 3),
                area.width * 3
            )
            y++
        }

        return inverseArray
    }

    private fun createBaseViewport(viewPort: ViewData): ViewportData {
        return ViewportData(
            OrthographicCamera(
                viewPort.bounds.width.toFloat(),
                viewPort.bounds.height.toFloat()
            ),
            null, null
        )
    }

    private fun createVirtualViewport(viewPort: ViewData): ViewportData {
        val camera = OrthographicCamera(
            viewPort.bounds.width.toFloat(),
            viewPort.bounds.height.toFloat()
        )
        val fboScaler = viewPort.fboScaler
        val frameBuffer = FrameBuffer(
            Pixmap.Format.RGBA8888,
            (viewPort.bounds.width * fboScaler).toInt(),
            (viewPort.bounds.height * fboScaler).toInt(),
            false
        )
        val textureRegion = TextureRegion(frameBuffer.colorBufferTexture)
        textureRegion.flip(false, false)

        return ViewportData(camera, frameBuffer, textureRegion)
    }

    private class ViewportData internal constructor(
        internal val camera: OrthographicCamera,
        internal val fbo: FrameBuffer?,
        internal val fboTexture: TextureRegion?) {

        internal fun activate(
            spriteBatch: SpriteBatch,
            shapeRenderer: ShapeRenderer,
            view: ViewData,
            clear: Boolean
        ) {
            val worldPosition = view.worldPosition
            val zoom = view.zoom
            val clearColor = view.clearColor
            val bounds = view.bounds

            camera.setToOrtho(true, bounds.width * zoom, bounds.height * zoom)
            camera.position.x = camera.position.x + worldPosition.x
            camera.position.y = camera.position.y + worldPosition.y
            camera.update()
            spriteBatch.projectionMatrix = camera.combined
            shapeRenderer.projectionMatrix = camera.combined

            fbo?.begin()

            if (clear) {
                Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a)
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
            }
        }

        internal fun dispose() =
            fbo?.dispose()
    }


    private fun getLibGDXTextureWrap(glConst: Int): TextureWrap =
        TextureWrap.values().firstOrNull { it.glEnum == glConst }
            ?: TextureWrap.Repeat

    private fun getLibGDXTextureFilter(glConst: Int): TextureFilter =
        TextureFilter.values().firstOrNull { it.glEnum == glConst }
            ?: TextureFilter.Linear

    private fun setColorAndBlendMode(renderColor: RGBColor, blendMode: BlendMode) {
        spriteBatch.setColor(renderColor.r, renderColor.g, renderColor.b, renderColor.a)
        if (activeBlend !== blendMode) {
            activeBlend = blendMode
            if (activeBlend !== BlendMode.NONE) {
                spriteBatch.enableBlending()
                spriteBatch.setBlendFunction(activeBlend.source, activeBlend.dest)
            } else {
                spriteBatch.disableBlending()
            }
        }
    }

    private fun setShaderForSpriteBatch(spriteRenderable: SpriteRenderable) {
        val shaderId = spriteRenderable.shaderId
        if (shaderId != activeShaderId) {
            if (shaderId < 0) {
                spriteBatch.shader = null
                activeShaderId = -1
            } else {
                val shaderProgram = shaders[shaderId]
                spriteBatch.shader = shaderProgram
                activeShaderId = shaderId
            }
        }
    }

    private val SHAPE_COLOR_1 = Color()
    private val SHAPE_COLOR_2 = Color()
    private val SHAPE_COLOR_3 = Color()
    private val SHAPE_COLOR_4 = Color()
    private fun getShapeColor(rgbColor: RGBColor, color: Color) =
        color.set(rgbColor.r, rgbColor.g, rgbColor.b, rgbColor.a)
}