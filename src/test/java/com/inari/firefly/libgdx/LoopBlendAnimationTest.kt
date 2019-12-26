package com.inari.firefly.libgdx

import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.inari.firefly.entity.Entity
import com.inari.firefly.external.ShapeType
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.shape.EShape
import com.inari.firefly.physics.animation.easing.EasedProperty
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.util.geom.Easing
import com.inari.util.graphics.RGBColor

class LoopBlendAnimationTest : GDXAppAdapter() {

    override val title: String = "LoopBlendAnimationTest"

    override fun init() {
        Entity.buildAndActivate {
            with(ETransform) {
                ff_Position.x = 50f
                ff_Position.y = 50f
            }
            with(EShape) {
                ff_Type = ShapeType.RECTANGLE
                ff_Fill = true
                ff_Color = RGBColor(1f, 1f, 1f, 1f)
                ff_Vertices = floatArrayOf(0f,0f,100f,100f)
            }
            with(EAnimation) {
                withActive(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = 1f
                    ff_EndValue = 0f
                    ff_Duration = 1000
                    ff_Easing = Easing.Type.LINEAR
                    ff_PropertyRef = EShape.Property.COLOR_RED
                }
                withActive(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = 1f
                    ff_EndValue = 0f
                    ff_Duration = 3000
                    ff_Easing = Easing.Type.LINEAR
                    ff_PropertyRef = EShape.Property.COLOR_GREEN
                }
                withActive(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = 1f
                    ff_EndValue = 0f
                    ff_Duration = 5000
                    ff_Easing= Easing.Type.LINEAR
                    ff_PropertyRef = EShape.Property.COLOR_BLUE
                }
            }
        }

        addExitKeyTrigger(Input.Keys.SPACE)
    }

}

fun main(args: Array<String>) {
    try {
        val config = LwjglApplicationConfiguration()
        config.resizable = false
        config.width = 704
        config.height = 480
        config.fullscreen = false
        LwjglApplication(LoopBlendAnimationTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}