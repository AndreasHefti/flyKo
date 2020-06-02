package com.inari.firefly.libgdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics
import com.inari.firefly.external.FFInput
import com.inari.firefly.external.FFInput.*
import com.inari.firefly.external.FFInput.ControllerInput.*
import com.inari.java.types.BitSet
import com.inari.util.collection.DynIntArray
import org.lwjgl.glfw.GLFW
import kotlin.experimental.and


object GDXInput : FFInput {

    override val xpos: Int
        get() = Gdx.input.x
    override val ypos: Int
        get() = Gdx.input.y
    override val dx: Int
        get() = Gdx.input.deltaX
    override val dy: Int
        get() = Gdx.input.deltaY

    override val implementations: List<InputImpl> = listOf(
            GLFWDesktopKeyboardInput)
    override val devices: MutableMap<String, InputDevice> = HashMap()

    override fun <T : InputDevice> createDevice(
            name: String,
            implementation: InputImpl,
            window: Long): T {

        return if (window < 0) {
            val w = (Gdx.graphics as Lwjgl3Graphics).window
            val instance = implementation.create<T>(w.windowHandle)
            devices[name] = instance
            instance
        } else {
            val instance = implementation.create<T>(window)
            devices[name] = instance
            instance
        }
    }

    override fun getDevice(name: String): InputDevice =
            devices[name] ?: FFInput.NULL_INPUT_DEVICE


    class GLFWDesktopKeyboardInput(override val window: Long) : KeyInput {
        override val name: String = "DEFAULT DESKTOP KEYBOARD INPUT"
        override val type: InputImpl = Companion

        private val buttonCodeMapping = DynIntArray(20, -1)
        private val pressedCodeMapping = BitSet()

        override fun buttonPressed(button: ButtonType): Boolean {
            val buttonCode = button.ordinal
            val keyCode = buttonCodeMapping[buttonCode]
            if (keyCode >= 0 && GLFW.glfwGetKey(window, keyCode) == 1)
                return true

            return false
        }

        override fun buttonTyped(button: ButtonType): Boolean {
            val buttonCode = button.ordinal
            val pressed = buttonPressed(button)
            if (pressed && pressedCodeMapping[buttonCode])
                return false

            pressedCodeMapping.set(buttonCode, pressed)
            return pressed
        }

        override fun mapKeyInput(buttonType: ButtonType, keyCode: Int) {
            buttonCodeMapping[buttonType.ordinal] = keyCode
        }
        companion object : FFInput.InputImpl {
            override val type = DeviceType.KEYBOARD
            override fun <T : InputDevice> create(window: Long): T = GLFWDesktopKeyboardInput(window) as T
        }
    }

    class GLFWControllerInput(override val window: Long) : ControllerInput {

        override val name: String = "Basic Controller Input"
        override val type: InputImpl = Companion
        override val controllerDefinitions: List<ControllerDef> get() = scanController()
        override var controller: ControllerDef
            get() = currentController
            set(value) { currentController = value }
        private var currentController = ControllerDef(-1, "NULL")
        private val buttonCodeMapping = DynIntArray(20, -1)
        private val hatCodeMapping = DynIntArray(20, -1)
        private val pressedCodeMapping = BitSet(255)

        init {
            mapHatInput(ButtonType.UP, GLFW.GLFW_HAT_UP)
            mapHatInput(ButtonType.RIGHT, GLFW.GLFW_HAT_RIGHT)
            mapHatInput(ButtonType.DOWN, GLFW.GLFW_HAT_DOWN)
            mapHatInput(ButtonType.LEFT, GLFW.GLFW_HAT_LEFT)
            mapButtonInput(ButtonType.BUTTON_A, GLFW.GLFW_GAMEPAD_BUTTON_A)
            mapButtonInput(ButtonType.BUTTON_B, GLFW.GLFW_GAMEPAD_BUTTON_B)
            mapButtonInput(ButtonType.BUTTON_X, GLFW.GLFW_GAMEPAD_BUTTON_X)
            mapButtonInput(ButtonType.BUTTON_Y, GLFW.GLFW_GAMEPAD_BUTTON_Y)
            mapButtonInput(ButtonType.QUIT, GLFW.GLFW_GAMEPAD_BUTTON_BACK)
            mapButtonInput(ButtonType.ENTER, GLFW.GLFW_GAMEPAD_BUTTON_START)
            mapButtonInput(ButtonType.FIRE_1, GLFW.GLFW_GAMEPAD_BUTTON_X)
            mapButtonInput(ButtonType.FIRE_2, GLFW.GLFW_GAMEPAD_BUTTON_Y)
            mapButtonInput(ButtonType.FIRE_1, GLFW.GLFW_GAMEPAD_BUTTON_A)
            mapButtonInput(ButtonType.FIRE_2, GLFW.GLFW_GAMEPAD_BUTTON_B)
        }

        override fun buttonPressed(button: ButtonType): Boolean {
            val buttonCode = button.ordinal

            if (!hatCodeMapping.isEmpty(buttonCode)) {
                val keyCode = hatCodeMapping[buttonCode]
                val hats = GLFW.glfwGetJoystickHats(currentController.id)!!
                return (hats[0] and keyCode.toByte() > 0)

            }
            val buttons = GLFW.glfwGetJoystickButtons(currentController.id)!!
            val keyCode = buttonCodeMapping[buttonCode]
            if (keyCode >= 0 && buttons[keyCode] == ONE)
                return true

            return false
        }

        override fun buttonTyped(button: ButtonType): Boolean {
            val buttonCode = button.ordinal
            val pressed = buttonPressed(button)
            if (pressed && pressedCodeMapping[buttonCode])
                return false

            pressedCodeMapping.set(buttonCode, pressed)
            return pressed
        }

        override fun mapButtonInput(buttonType: ButtonType, padButton: Int) {
            buttonCodeMapping[buttonType.ordinal] = padButton
        }

        override fun mapHatInput(buttonType: ButtonType, padHat: Int) {
            hatCodeMapping[buttonType.ordinal] = padHat
        }

        override fun axis(axisType: Int): Float =
                GLFW.glfwGetJoystickAxes(currentController.id)!![axisType]

        private fun scanController(): List<ControllerDef> {
            val list: MutableList<ControllerDef> = ArrayList()
            for (i in 0 .. 4) {
                if (GLFW.glfwJoystickPresent(i)) {
                    list.add(ControllerDef(i, GLFW.glfwGetJoystickName(i)!!))
                }
            }
            return list
        }

        companion object : InputImpl {
            private const val ONE = 1.toByte()
            override val type = DeviceType.JOYSTICK_PAD
            override fun <T : InputDevice> create(window: Long): T = GLFWControllerInput(window) as T
        }
    }


}