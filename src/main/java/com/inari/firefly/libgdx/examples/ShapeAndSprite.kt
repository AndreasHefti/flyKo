package com.inari.firefly.libgdx.examples

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.external.ShapeType
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.rendering.RenderingSystem
import com.inari.firefly.graphics.shape.EShape
import com.inari.firefly.graphics.view.View
import com.inari.firefly.libgdx.GDXAppAdapter
import com.inari.firefly.physics.animation.AnimationSystem
import com.inari.firefly.system.FFInfoSystem
import com.inari.firefly.system.FrameRateInfo
import com.inari.util.graphics.RGBColor

class ShapeAndSprite : GDXAppAdapter()  {

    override val title: String = "ShapeAndSprite"

    override fun init() {
        RenderingSystem
        FFContext.loadSystem(EntitySystem)
        FFContext.loadSystem(AnimationSystem)

        View.buildAndActivate {
            ff_Name = "TestView"
            ff_Bounds(10, 10, 800, 600)
            ff_FboScale = 1.0f
        }

        Entity.buildAndActivate {
            ff_With(ETransform) {
                ff_View("TestView")
                ff_Position(0, 0)
            }
            ff_With(EShape) {
                ff_Type = ShapeType.POLYGON
                ff_Fill = true
                ff_Color = RGBColor.RED
                ff_Vertices = floatArrayOf(0f, 0f, 20f,0f,20f,20f, 0f,0f)
            }
//            ff_With(EShape) {
//                ff_Type = ShapeType.TRIANGLE
//                ff_Color = RGBColor.RED
//                ff_Fill = true
//                ff_Vertices = floatArrayOf(0f, 0f, 120f, 120f, 20f, 20f)
//            }
        }
    }


}

fun main(args: Array<String>) {
    try {
        val config = LwjglApplicationConfiguration()
        config.resizable = false
        config.width = 800
        config.height = 600
        config.fullscreen = false
        LwjglApplication(ShapeAndSprite(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}