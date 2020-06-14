package com.inari.firefly.libgdx.examples

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.SYSTEM_FONT
import com.inari.firefly.entity.Entity
import com.inari.firefly.external.FFInput
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.text.EText
import com.inari.firefly.libgdx.DesktopAppAdapter
import com.inari.firefly.libgdx.DesktopInput
import com.inari.util.Call
import org.lwjgl.glfw.GLFW

class ControllerInputTest : DesktopAppAdapter() {

    override val title: String = this.javaClass.simpleName

    private var text = StringBuilder("")
    private var textId = NO_COMP_ID
    private val updateCall: Call = {
        val textEntity = FFContext[textId, EText]
        val keyInput = FFContext.input.getDevice("KeyInput")
        when {
            keyInput.buttonPressed(FFInput.ButtonType.UP) -> textEntity.text.clear().append("UP")
            keyInput.buttonPressed(FFInput.ButtonType.RIGHT) -> textEntity.text.clear().append("RIGHT")
            keyInput.buttonPressed(FFInput.ButtonType.DOWN) -> textEntity.text.clear().append("DOWN")
            keyInput.buttonPressed(FFInput.ButtonType.LEFT) -> textEntity.text.clear().append("LEFT")
            keyInput.buttonPressed(FFInput.ButtonType.FIRE_1) -> textEntity.text.clear().append("FIRE_1")
            keyInput.buttonPressed(FFInput.ButtonType.FIRE_2) -> textEntity.text.clear().append("FIRE_2")
            keyInput.buttonPressed(FFInput.ButtonType.QUIT) -> textEntity.text.clear().append("QUIT")
            else -> textEntity.text.clear().append("--")
        }

    }

    override fun init() {

        val keyInput1 = FFContext.input.createDevice<DesktopInput.GLFWDesktopKeyboardInput>(
                "KeyInput1",
                DesktopInput.GLFWDesktopKeyboardInput)
        val keyInput2 = FFContext.input.createDevice<DesktopInput.GLFWDesktopKeyboardInput>(
                "KeyInput2",
                DesktopInput.GLFWDesktopKeyboardInput)
        val keyInput3 = FFContext.input.createDevice<DesktopInput.GLFWControllerInput>(
                "KeyInput3",
                DesktopInput.GLFWControllerInput)

        keyInput1.mapKeyInput(FFInput.ButtonType.UP, GLFW.GLFW_KEY_W)
        keyInput1.mapKeyInput(FFInput.ButtonType.RIGHT, GLFW.GLFW_KEY_D)
        keyInput1.mapKeyInput(FFInput.ButtonType.DOWN, GLFW.GLFW_KEY_S)
        keyInput1.mapKeyInput(FFInput.ButtonType.LEFT, GLFW.GLFW_KEY_A)
        keyInput1.mapKeyInput(FFInput.ButtonType.FIRE_1, GLFW.GLFW_KEY_SPACE)
        keyInput1.mapKeyInput(FFInput.ButtonType.FIRE_2, GLFW.GLFW_KEY_RIGHT_ALT)
        keyInput1.mapKeyInput(FFInput.ButtonType.QUIT, GLFW.GLFW_KEY_ESCAPE)

        keyInput2.mapKeyInput(FFInput.ButtonType.UP, GLFW.GLFW_KEY_UP)
        keyInput2.mapKeyInput(FFInput.ButtonType.RIGHT, GLFW.GLFW_KEY_RIGHT)
        keyInput2.mapKeyInput(FFInput.ButtonType.DOWN, GLFW.GLFW_KEY_DOWN)
        keyInput2.mapKeyInput(FFInput.ButtonType.LEFT, GLFW.GLFW_KEY_LEFT)

        val controller = keyInput3.controllerDefinitions
        if (controller.isNotEmpty()) {
            keyInput3.controller = controller[0]
            FFContext.input.createOrAdapter("KeyInputOr", "KeyInput1", "KeyInput2")
            FFContext.input.createOrAdapter("KeyInput", "KeyInputOr", "KeyInput3")
        } else {
            FFContext.input.createOrAdapter("KeyInput", "KeyInput1", "KeyInput2")
        }

        textId = Entity.buildAndActivate {
            ff_With(ETransform) {
                ff_Position(100, 100)
            }
            ff_With(EText) {
                ff_FontAsset(SYSTEM_FONT)
                ff_Text.append(text)
                this@ControllerInputTest.text = ff_Text
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
        Lwjgl3Application(ControllerInputTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}