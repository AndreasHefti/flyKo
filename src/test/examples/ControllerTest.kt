import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.SYSTEM_FONT
import com.inari.firefly.entity.Entity
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.text.EText
import com.inari.firefly.libgdx.DesktopAppAdapter
import com.inari.util.Call
import org.lwjgl.glfw.GLFW
import java.nio.ByteBuffer
import java.nio.FloatBuffer

class ControllerTest : DesktopAppAdapter() {

    override val title: String = this::class.simpleName!!
    private var text1 = StringBuilder("")
    private var text2 = StringBuilder("")
    private var text3 = StringBuilder("")
    private var textId1 = NO_COMP_ID
    private var textId2 = NO_COMP_ID
    private var textId3 = NO_COMP_ID



    private val updateCall: Call = {
        val bytes: ByteBuffer = GLFW.glfwGetJoystickButtons(GLFW.GLFW_JOYSTICK_1)!!
        val axes: FloatBuffer = GLFW.glfwGetJoystickAxes(GLFW.GLFW_JOYSTICK_1)!!
        val hats: ByteBuffer = GLFW.glfwGetJoystickHats(GLFW.GLFW_JOYSTICK_1)!!
        val bytesArray = ByteArray(bytes.remaining())
        val floatArray = FloatArray(axes.remaining())
        val hatsArray = ByteArray(hats.remaining())
        bytes.get(bytesArray, 0, bytesArray.size)
        axes.get(floatArray, 0, floatArray.size)
        hats.get(hatsArray, 0, hatsArray.size)
        text1.clear().append(bytesArray.contentToString())
        text2.clear().append(floatArray.contentToString())
        if (hats.hasRemaining())
            text3.clear().append(hats[0])

    }

    override fun init() {
        textId1 = Entity.buildAndActivate {
            component(ETransform) {
                position(100, 100)
            }
            component(EText) {
                fontAsset(SYSTEM_FONT)
                text.append(text1)
                this@ControllerTest.text1 = text
            }
        }
        textId2 = Entity.buildAndActivate {
            component(ETransform) {
                position(100, 150)
            }
            component(EText) {
                fontAsset(SYSTEM_FONT)
                text.append(text2)
                this@ControllerTest.text2 = text
            }
        }
        textId3 = Entity.buildAndActivate {
            component(ETransform) {
                position(100, 200)
            }
            component(EText) {
                fontAsset(SYSTEM_FONT)
                text.append(text3)
                this@ControllerTest.text3 = text
            }
        }
        FFContext.registerListener(FFApp.UpdateEvent, updateCall)
    }

}

fun main(args: Array<String>) {
    try {
        val config = Lwjgl3ApplicationConfiguration()
        config.setResizable(true)
        config.setWindowedMode(704, 480)
        Lwjgl3Application(ControllerTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}