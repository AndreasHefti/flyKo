package com.inari.firefly.libgdx.examples

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.external.ShapeType
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.rendering.RenderingSystem
import com.inari.firefly.graphics.shape.EShape
import com.inari.firefly.libgdx.GDXAppAdapter
import com.inari.firefly.physics.animation.AnimationSystem
import com.inari.firefly.physics.animation.easing.EasedProperty
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.firefly.system.FFInfoSystem
import com.inari.firefly.system.FrameRateInfo
import com.inari.util.geom.Easing

class ShapeTransformTest : GDXAppAdapter() {

    override val title: String = "ShapeTransformTest"

    override fun init() {
        FFInfoSystem
                .addInfo(FrameRateInfo)
                .activate()
        RenderingSystem
        FFContext.loadSystem(EntitySystem)
        FFContext.loadSystem(AnimationSystem)

        Entity.buildAndActivate {
            ff_With(ETransform) {
                ff_Position(100f, 50f)
                ff_Pivot(110f, 60f)
                ff_Rotation = 0f
            }
            ff_With(EShape) {
                ff_Type = ShapeType.RECTANGLE
                ff_Fill = true
                ff_Color(1f, 0f, 0f, 1f)
                ff_Vertices = floatArrayOf(0f, 0f, 20f, 20f)
            }
            ff_With(EAnimation) {
                withActiveAnimation(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = 0f
                    ff_EndValue = 180f
                    ff_Duration = 1000
                    ff_Easing = Easing.Type.LINEAR
                    ff_PropertyRef = ETransform.Property.ROTATION
                }
                withActiveAnimation(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = 100f
                    ff_EndValue = 600f
                    ff_Duration = 3000
                    ff_Easing = Easing.Type.SIN_IN_OUT
                    ff_PropertyRef = ETransform.Property.POSITION_X
                }
                withActiveAnimation(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = 110f
                    ff_EndValue = 610f
                    ff_Duration = 3000
                    ff_Easing = Easing.Type.SIN_IN_OUT
                    ff_PropertyRef = ETransform.Property.PIVOT_X
                }
            }
        }


        Entity.buildAndActivate {
            ff_With(ETransform) {
                ff_Position(100f, 150f)
                ff_Pivot(110f, 160f)
                ff_Scale(1f, 1f)
            }
            ff_With(EShape) {
                ff_Type = ShapeType.TRIANGLE
                ff_Fill = true
                ff_Color(1f, 0f, 0f, 1f)
                ff_Vertices = floatArrayOf(10f, 0f, 20f, 20f, 0f, 20f)
            }
            ff_With(EAnimation) {
                withActiveAnimation(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = .5f
                    ff_EndValue = 2f
                    ff_Duration = 1000
                    ff_Easing = Easing.Type.LINEAR
                    ff_PropertyRef = ETransform.Property.SCALE_X
                }
                withActiveAnimation(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = .5f
                    ff_EndValue = 2f
                    ff_Duration = 1000
                    ff_Easing = Easing.Type.LINEAR
                    ff_PropertyRef = ETransform.Property.SCALE_Y
                }
                withActiveAnimation(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = 100f
                    ff_EndValue = 600f
                    ff_Duration = 3000
                    ff_Easing = Easing.Type.SIN_OUT
                    ff_PropertyRef = ETransform.Property.POSITION_X
                }
                withActiveAnimation(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = 110f
                    ff_EndValue = 610f
                    ff_Duration = 3000
                    ff_Easing = Easing.Type.SIN_OUT
                    ff_PropertyRef = ETransform.Property.PIVOT_X
                }
            }
        }


        Entity.buildAndActivate {
            ff_With(ETransform) {
                ff_Position(100f, 300f)
                ff_Pivot(110f, 160f)
                ff_Scale(1f, 1f)
            }
            ff_With(EShape) {
                ff_Type = ShapeType.CIRCLE
                ff_Segments = 20
                ff_Color(1f, 0f, 0f, 1f)
                ff_Vertices = floatArrayOf(10f, 0f, 0f)
            }
            ff_With(EAnimation) {
                withActiveAnimation(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = 100f
                    ff_EndValue = 600f
                    ff_Duration = 3000
                    ff_Easing = Easing.Type.SIN_IN
                    ff_PropertyRef = ETransform.Property.POSITION_X
                }
                withActiveAnimation(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = 250f
                    ff_EndValue = 350f
                    ff_Duration = 1500
                    ff_Easing = Easing.Type.SIN_IN_OUT
                    ff_PropertyRef = ETransform.Property.POSITION_Y
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    try {
        val config = LwjglApplicationConfiguration()
        config.resizable = false
        config.width = 704
        config.height = 480
        config.fullscreen = false
        LwjglApplication(ShapeTransformTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}