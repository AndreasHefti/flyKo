import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.external.ShapeType
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.rendering.RenderingSystem
import com.inari.firefly.graphics.shape.EShape
import com.inari.firefly.graphics.view.View
import com.inari.firefly.libgdx.DesktopAppAdapter
import com.inari.firefly.physics.animation.AnimationSystem
import com.inari.util.graphics.RGBColor

class ShapeAndSprite : DesktopAppAdapter()  {

    override val title: String = "ShapeAndSprite"

    override fun init() {
        RenderingSystem
        FFContext.loadSystem(EntitySystem)
        FFContext.loadSystem(AnimationSystem)

        View.buildAndActivate {
            name = "TestView"
            bounds(10, 10, 800, 600)
            fboScale = 1.0f
        }

        Entity.buildAndActivate {
            component(ETransform) {
                view("TestView")
                position(0, 0)
            }
            component(EShape) {
                type = ShapeType.POLYGON
                fill = true
                color = RGBColor.RED
                vertices = floatArrayOf(0f, 0f, 20f,0f,20f,20f, 0f,0f)
            }
//            With(EShape) {
//                Type = ShapeType.TRIANGLE
//                Color = RGBColor.RED
//                Fill = true
//                Vertices = floatArrayOf(0f, 0f, 120f, 120f, 20f, 20f)
//            }
        }
    }


}

fun main(args: Array<String>) {
    try {
        val config = Lwjgl3ApplicationConfiguration()
        config.setResizable(true)
        config.setWindowedMode(800, 600)
        Lwjgl3Application(ShapeAndSprite(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}