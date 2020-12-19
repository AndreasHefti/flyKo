import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.entity.Entity
import com.inari.firefly.external.ShapeType
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.shape.EShape
import com.inari.firefly.libgdx.DesktopAppAdapter

class DynamicViewTest : DesktopAppAdapter() {

    override val title: String = "DynamicViewTest"

    override fun init() {

        Entity.buildAndActivate {
            component(ETransform) {
                position(0,0)
            }
            component(EShape) {
                type = ShapeType.RECTANGLE
                vertices = floatArrayOf(0f, 0f, 400f, 200f, -10f, -10f, 420f, 220f)
                color(1f,0f,0f,1f)
                fill = false
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        fitBaseViewportToScreen(width, height, 400, 200, false)
    }

}

fun main(args: Array<String>) {
    try {
        val config = Lwjgl3ApplicationConfiguration()
        config.setResizable(true)
        config.setWindowedMode(400, 200)
        Lwjgl3Application(DynamicViewTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}