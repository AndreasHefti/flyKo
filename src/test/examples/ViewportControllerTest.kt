import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.FFContext
import com.inari.firefly.control.Controller
import com.inari.firefly.control.ControllerSystem
import com.inari.firefly.entity.Entity
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.TextureAsset
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.graphics.sprite.SpriteAsset
import com.inari.firefly.graphics.view.View
import com.inari.firefly.libgdx.DesktopAppAdapter
import com.inari.firefly.system.FFInfoSystem
import com.inari.firefly.system.FrameRateInfo
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.geom.PositionF

class ViewportControllerTest : DesktopAppAdapter() {

    override val title: String = "ViewportControllerTest"

    override fun init() {

        FFInfoSystem
            .addInfo(FrameRateInfo)
            .activate()
        ControllerSystem

        TextureAsset.build {
            name = "TEST"
            resourceName = "firefly/inari.png"
        }

        SpriteAsset.buildAndActivate {
            name = "TestSprite"
            texture("TEST")
            textureRegion(0,0,100,100)
        }

        val viewId = View.buildAndActivate {
            worldPosition(0, 0)
            bounds(0, 0, 200, 200)
            blendMode = BlendMode.NORMAL_ALPHA
            zoom = .5f
             withActiveController(TestController) {}
        }

        Entity.buildAndActivate {
            component(ETransform) {
                view(viewId)
                position(0,0)
            }
            component(ESprite) {
                sprite("TestSprite")
            }
        }

    }

}

class TestController private constructor() : Controller() {

    private val pos = PositionF()
    //private var view: View? = null

    override fun update(componentId: Int) {
        val view = FFContext[View, componentId]
        if (view.worldPosition.x < 50)
            view.worldPosition.x += 2
        else if (view.worldPosition.x > 50)
            view.worldPosition.x -= 2
    }

    companion object : SystemComponentSubType<Controller, TestController>(Controller, TestController::class) {
        override fun createEmpty() = TestController()
    }
}

fun main(args: Array<String>) {
    try {
        val config = Lwjgl3ApplicationConfiguration()
        config.setResizable(true)
        config.setWindowedMode(800, 600)
        Lwjgl3Application(ViewportControllerTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}