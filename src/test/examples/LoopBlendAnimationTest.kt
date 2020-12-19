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
            component(ETransform) {
                position.x = 50f
                position.y = 50f
            }
            component(EShape) {
                type = ShapeType.RECTANGLE
                fill = true
                color(1f, 1f, 1f, 1f)
                vertices = floatArrayOf(0f,0f,100f,100f)
            }
            component(EAnimation) {
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 1f
                    endValue = 0f
                    duration = 1000
                    easing = Easing.Type.LINEAR
                    propertyRef = EShape.Property.COLOR_RED
                }
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 1f
                    endValue = 0f
                    duration = 3000
                    easing = Easing.Type.LINEAR
                    propertyRef = EShape.Property.COLOR_GREEN
                }
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 1f
                    endValue = 0f
                    duration = 5000
                    easing= Easing.Type.LINEAR
                    propertyRef = EShape.Property.COLOR_BLUE
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