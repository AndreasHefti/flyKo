package com.inari.firefly.libgdx.examples

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.inari.firefly.FFContext
import com.inari.firefly.SYSTEM_FONT
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.external.ShapeType
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.rendering.RenderingSystem
import com.inari.firefly.graphics.rendering.SimpleTextRenderer
import com.inari.firefly.graphics.shape.EShape
import com.inari.firefly.graphics.text.EText
import com.inari.firefly.libgdx.GDXAppAdapter

import com.inari.firefly.physics.animation.AnimationSystem
import com.inari.firefly.physics.animation.easing.EasedProperty
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.firefly.system.FFInfoSystem
import com.inari.firefly.system.FrameRateInfo
import com.inari.util.geom.Easing


class EasingTest2 : GDXAppAdapter() {

    override val title: String = "EasingTest2"

    override fun init() {
        FFInfoSystem
            .addInfo(FrameRateInfo)
            .activate()
        RenderingSystem
        FFContext.loadSystem(EntitySystem)
        FFContext.loadSystem(AnimationSystem)

        for ((index, easingType) in Easing.Type.values().withIndex()) {
            createEasingAnimation(easingType, index)
        }
    }

    private fun createEasingAnimation(type: Easing.Type, position: Int) {
        val ypos = position.toFloat() * (20f + 10f) + 50f
        Entity.buildAndActivate {
            ff_With(ETransform) {
                ff_View(0)
                ff_Position(10f, ypos)
            }
            ff_With(EText) {
                ff_Renderer(SimpleTextRenderer)
                ff_FontAsset(SYSTEM_FONT)
                ff_TextBuffer.append(type.name.replace("_", "-"))
                ff_Blend = BlendMode.NORMAL_ALPHA
                ff_Tint(1f, 1f, 1f, 1f)
            }
        }
        Entity.buildAndActivate {
            ff_With(ETransform) {
                ff_View(0)
            }
            ff_With(EShape) {
                ff_Type = ShapeType.RECTANGLE
                ff_Fill = true
                ff_Color(1f, 0f, 0f, 1f)
                ff_Vertices = floatArrayOf(100f, ypos, 20f, 20f)
            }
            ff_With(EAnimation) {
                ff_WithActiveAnimation(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = 100f
                    ff_EndValue = 400f
                    ff_Duration = 5000
                    ff_Easing = type
                    ff_PropertyRef = ETransform.Property.POSITION_X
                }
            }
        }
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            try {
                val config = LwjglApplicationConfiguration()
                config.resizable = true
                config.width = 800
                config.height = 900
                LwjglApplication(EasingTest2(), config)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }
}
