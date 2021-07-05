import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.FFContext
import com.inari.firefly.NO_CAMERA_PIVOT
import com.inari.firefly.component.CompId
import com.inari.firefly.control.ControllerSystem
import com.inari.firefly.control.SystemComponentController
import com.inari.firefly.entity.Entity
import com.inari.firefly.external.ViewData
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.TextureAsset
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.graphics.sprite.SpriteAsset
import com.inari.firefly.graphics.view.View
import com.inari.firefly.graphics.view.ViewChangeEvent
import com.inari.firefly.graphics.view.ViewEvent
import com.inari.firefly.graphics.view.camera.CameraPivot
import com.inari.firefly.libgdx.DesktopAppAdapter
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.geom.PositionF
import com.inari.util.geom.Rectangle
import com.inari.util.graphics.RGBColor
import kotlin.math.ceil
import kotlin.math.floor

class ViewportControllerTest : DesktopAppAdapter() {

    override val title: String = "ViewportControllerTest"

    override fun init() {

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
            withController(TestController) {}
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

class TestController private constructor() : SystemComponentController() {

    private val pos = PositionF()
    private var view: View? = null

    private val activation: ViewEvent.Listener = object : ViewEvent.Listener {
        override fun invoke(id: CompId, viewPort: ViewData, type: ViewEvent.Type) {
            if (controlledComponentId == id) {
                when (type) {
                    ViewEvent.Type.VIEW_ACTIVATED -> {
                        FFContext.activate(componentId)
                        view = FFContext[controlledComponentId]
                    }
                    ViewEvent.Type.VIEW_DISPOSED -> {
                        FFContext.deactivate(componentId)
                        view = null
                    }
                    else -> {}
                }
            }
        }
    }

    override fun init() {
        super.init()
        FFContext.registerListener(ViewEvent, activation)
    }

    override fun dispose() {
        super.dispose()
        FFContext.disposeListener(ViewEvent, activation)
    }

    override fun update() {
        val view = this.view ?: return
        if (view.worldPosition.x < 50)
            view.worldPosition.x++
        else if (view.worldPosition.x > 50)
            view.worldPosition.x--
    }

    companion object : SystemComponentSubType<SystemComponentController, TestController>(
        SystemComponentController, TestController::class) {
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