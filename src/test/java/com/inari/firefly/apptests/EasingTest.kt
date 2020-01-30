package com.inari.firefly.apptests

import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.inari.firefly.entity.Entity
import com.inari.firefly.external.ShapeType
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.shape.EShape
import com.inari.firefly.libgdx.GDXAppAdapter
import com.inari.firefly.physics.animation.easing.EasedProperty
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.util.geom.Easing
import com.inari.util.graphics.RGBColor

class EasingTest : GDXAppAdapter() {

    override val title: String = "EasingTest"

    override fun init() {
        Entity.buildAndActivate {
            ff_With(ETransform) {
                ff_View(0)
                ff_Position.x = 50f
                ff_Position.y = 50f
            }
            ff_With(EShape) {
                ff_Type = ShapeType.CIRCLE
                ff_Segments = 20
                ff_Fill = true
                ff_Color = RGBColor(1f, 1f, 1f, 1f)
                ff_Vertices = floatArrayOf(0f,0f,10f)
            }
            ff_With(EAnimation) {
                withActiveAnimation(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = 50f
                    ff_EndValue = 100f
                    ff_Duration = 5000
                    ff_Easing = Easing.Type.LINEAR
                    ff_PropertyRef = ETransform.Property.POSITION_X

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
        LwjglApplication(EasingTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}