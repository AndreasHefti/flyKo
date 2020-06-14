package com.inari.firefly.libgdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.external.*
import com.inari.firefly.external.FFInput.*
import com.inari.firefly.external.FFInput.Companion.ACTION_PRESS
import com.inari.firefly.external.FFInput.Companion.ACTION_TYPED
import com.inari.firefly.external.FFInput.ControllerInput.*
import com.inari.java.types.BitSet
import com.inari.util.Call
import com.inari.util.collection.DynIntArray
import org.lwjgl.glfw.GLFW
import java.lang.IllegalArgumentException
import kotlin.experimental.and


object DesktopInput : FFInput {

    override val xpos: Int
        get() = Gdx.input.x
    override val ypos: Int
        get() = Gdx.input.y
    override val dx: Int
        get() = Gdx.input.deltaX
    override val dy: Int
        get() = Gdx.input.deltaY

    override val implementations: List<InputImpl> = listOf(
            GLFWDesktopKeyboardInput,
            GLFWControllerInput)

    override val devices: MutableMap<String, InputDevice> = HashMap()

    init {
        devices[FFInput.VOID_INPUT_DEVICE] = VOIDAdapter()
    }

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

    override fun createOrAdapter(name: String, a: String, b: String): ORAdapter {
        val adapter = ORAdapter(getDevice(a), getDevice(b), name)
        devices[name] = adapter
        return adapter
    }

    override fun getDevice(name: String): InputDevice =
            devices[name] ?: devices[FFInput.VOID_INPUT_DEVICE]!!

    override fun clearDevice(name: String) {
        devices.remove(name)
    }

    override fun setKeyCallback(callback: KeyCallback) {
        val w = (Gdx.graphics as Lwjgl3Graphics).window.windowHandle
        GLFW.glfwSetKeyCallback(w) { _, key, scancode, action, _ -> callback.invoke(key, scancode, action) }
    }

    override fun setMouseButtonCallback(callback: MouseCallback) {
        val w = (Gdx.graphics as Lwjgl3Graphics).window.windowHandle
        GLFW.glfwSetMouseButtonCallback(w) { _, key, action, _ -> callback.invoke(key, action) }
    }

    override fun setJoystickConnectionCallback(callback: JoystickConnectionCallback) {
        GLFW.glfwSetJoystickCallback { joystick, action -> callback.invoke(joystick, action) }
    }

    private var buttonCallbackUpdate: Call = {}
    override fun setButtonCallback(deviceName: String, callback: ButtonCallback) {
        if (deviceName in devices) {
            val device = devices[deviceName]!!
            val buttonTypes = ButtonType.values()
            buttonCallbackUpdate = {
                buttonTypes.forEach {
                    if (device.buttonPressed(it))
                        callback.invoke(it, ACTION_TYPED)
                    if (device.buttonPressed(it))
                        callback.invoke(it, ACTION_PRESS)
                }
            }
            FFContext.registerListener(FFApp.UpdateEvent, buttonCallbackUpdate)
        } else throw IllegalArgumentException("No device with name: $deviceName found")
    }

    override fun resetInputCallbacks() {
        val w = (Gdx.graphics as Lwjgl3Graphics).window.windowHandle
        GLFW.glfwSetKeyCallback(w, null)
        GLFW.glfwSetMouseButtonCallback(w, null)
        GLFW.glfwSetJoystickCallback(null)
        FFContext.disposeListener(FFApp.UpdateEvent, buttonCallbackUpdate)
        buttonCallbackUpdate = {}
    }

    class GLFWDesktopKeyboardInput(override val window: Long) : KeyInput {
        override val name: String = "DEFAULT DESKTOP KEYBOARD INPUT"
        override val type: InputImpl = Companion

        private val buttonCodeMapping = DynIntArray(ButtonType.values().size, -1)
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
        companion object : InputImpl {
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
        private val buttonCodeMapping = DynIntArray(ButtonType.values().size, -1)
        private val hatCodeMapping = DynIntArray(ButtonType.values().size, -1)
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