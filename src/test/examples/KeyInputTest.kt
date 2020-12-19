import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.SYSTEM_FONT
import com.inari.firefly.entity.Entity
import com.inari.firefly.external.FFInput.ButtonType.*
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.text.EText
import com.inari.firefly.libgdx.DesktopAppAdapter
import com.inari.firefly.libgdx.DesktopInput
import com.inari.util.Call
import org.lwjgl.glfw.GLFW


class KeyInputTest : DesktopAppAdapter() {

    override val title: String = this.javaClass.simpleName

    private var text = StringBuilder("")
    private var textSPACE = StringBuilder("")
    private var textId = NO_COMP_ID
    private var textSPACEId = NO_COMP_ID
    private val updateCall: Call = {
        val textEntity = FFContext[textId, EText]
        val textSPACEEntity = FFContext[textSPACEId, EText]
        val keyInput = FFContext.input.getDevice("KeyInput")
        when {
         //   keyInput.buttonPressed(UP) -> textEntity.text.clear().append("UP")
            keyInput.buttonPressed(RIGHT) -> textEntity.text.clear().append("RIGHT")
         //   keyInput.buttonPressed(DOWN) -> textEntity.text.clear().append("DOWN")
         //   keyInput.buttonPressed(LEFT) -> textEntity.text.clear().append("LEFT")
            else -> textEntity.text.clear().append("--")
        }
        when {
            keyInput.buttonPressed(FIRE_1) -> textSPACEEntity.text.clear().append("SPACE")
            else -> textSPACEEntity.text.clear().append("--")
        }

    }

    override fun init() {

        val keyInput1 = FFContext.input.createDevice<DesktopInput.GLFWDesktopKeyboardInput>(
                "KeyInput1",
                DesktopInput.GLFWDesktopKeyboardInput)
//        val keyInput2 = FFContext.input.createDevice<DesktopInput.GLFWDesktopKeyboardInput>(
//                "KeyInput2",
//                DesktopInput.GLFWDesktopKeyboardInput)

        keyInput1.mapKeyInput(UP, GLFW.GLFW_KEY_W)
        keyInput1.mapKeyInput(RIGHT, GLFW.GLFW_KEY_D)
        keyInput1.mapKeyInput(DOWN, GLFW.GLFW_KEY_S)
        keyInput1.mapKeyInput(LEFT, GLFW.GLFW_KEY_A)
        keyInput1.mapKeyInput(FIRE_1, GLFW.GLFW_KEY_SPACE)

//        keyInput2.mapKeyInput(UP, GLFW.GLFW_KEY_UP)
//        keyInput2.mapKeyInput(RIGHT, GLFW.GLFW_KEY_RIGHT)
//        keyInput2.mapKeyInput(DOWN, GLFW.GLFW_KEY_DOWN)
//        keyInput2.mapKeyInput(LEFT, GLFW.GLFW_KEY_LEFT)

        FFContext.input.createOrAdapter("KeyInput", "KeyInput1", "KeyInput2")

        textId = Entity.buildAndActivate {
            component(ETransform) {
                position(100, 100)
            }
            component(EText) {
                fontAsset(SYSTEM_FONT)
                text.append(text)
                this@KeyInputTest.text = text
            }
        }

        textSPACEId = Entity.buildAndActivate {
            component(ETransform) {
                position(100, 200)
            }
            component(EText) {
                fontAsset(SYSTEM_FONT)
                text.append(textSPACE)
                this@KeyInputTest.textSPACE = text
            }
        }

        FFContext.registerListener(FFApp.UpdateEvent, updateCall)
    }

}

fun main() {
    try {
        val config = Lwjgl3ApplicationConfiguration()
        config.setResizable(true)
        config.setWindowedMode(704, 480)
        Lwjgl3Application(KeyInputTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}