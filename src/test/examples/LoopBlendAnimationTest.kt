import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.entity.Entity
import com.inari.firefly.external.ShapeType
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.shape.EShape
import com.inari.firefly.libgdx.DesktopAppAdapter
import com.inari.firefly.physics.animation.easing.EasedProperty
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.util.geom.Easing

class LoopBlendAnimationTest : DesktopAppAdapter() {

    override val title: String = "LoopBlendAnimationTest"

    override fun init() {
        Entity.buildAndActivate {
            ff_With(ETransform) {
                ff_Position.x = 50f
                ff_Position.y = 50f
            }
            ff_With(EShape) {
                ff_Type = ShapeType.RECTANGLE
                ff_Fill = true
                ff_Color(1f, 1f, 1f, 1f)
                ff_Vertices = floatArrayOf(0f,0f,100f,100f)
            }
            ff_With(EAnimation) {
                ff_WithActiveAnimation(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = 1f
                    ff_EndValue = 0f
                    ff_Duration = 1000
                    ff_Easing = Easing.Type.LINEAR
                    ff_PropertyRef = EShape.Property.COLOR_RED
                }
                ff_WithActiveAnimation(EasedProperty) {
                    ff_Looping = true
                    ff_InverseOnLoop = true
                    ff_StartValue = 1f
                    ff_EndValue = 0f
                    ff_Duration = 3000
                    ff_Easing = Easing.Type.LINEAR
                    ff_PropertyRef = EShape.Property.COLOR_GREEN
                }
                ff_WithActiveAnimation(EasedProperty) {
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
        val config = Lwjgl3ApplicationConfiguration()
        config.setResizable(true)
        config.setWindowedMode(704, 480)
        Lwjgl3Application(LoopBlendAnimationTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}