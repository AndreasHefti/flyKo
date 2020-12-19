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
import com.inari.util.graphics.RGBColor

class EasingTest : DesktopAppAdapter() {

    override val title: String = "EasingTest"

    override fun init() {
        Entity.buildAndActivate {
            component(ETransform) {
                view(0)
                position.x = 50f
                position.y = 50f
            }
            component(EShape) {
                type = ShapeType.CIRCLE
                segments = 20
                fill = true
                color = RGBColor(1f, 1f, 1f, 1f)
                vertices = floatArrayOf(0f,0f,10f)
            }
            component(EAnimation) {
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 50f
                    endValue = 100f
                    duration = 5000
                    easing = Easing.Type.LINEAR
                    propertyRef = ETransform.Property.POSITION_X

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

        Lwjgl3Application(EasingTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}