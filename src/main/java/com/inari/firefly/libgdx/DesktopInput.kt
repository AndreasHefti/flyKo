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
import com.inari.util.BitSet
import com.inari.util.Call
import com.inari.util.collection.DynIntArray
import org.lwjgl.glfw.GLFW
import java.lang.IllegalArgumentException
import java.nio.ByteBuffer
import java.nio.FloatBuffer
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
            @Suppress("UNCHECKED_CAST")
            override fun <T : InputDevice> create(window: Long): T = GLFWDesktopKeyboardInput(window) as T
        }
    }

    class GLFWControllerInput(override val window: Long) : ControllerInput {

        override val name: String = "Basic Controller Input"
        override val type: InputImpl = Companion
        override var slot: Int = -1
            set(value) {
                if (field != value) {
                    if (GLFW.glfwJoystickPresent(value)) {
                        currentController = ControllerDef(value, GLFW.glfwGetJoystickName(value)!!)
                        buttons = GLFW.glfwGetJoystickButtons(currentController.id)!!
                        axis = GLFW.glfwGetJoystickAxes(currentController.id)!!
                        hats = GLFW.glfwGetJoystickHats(currentController.id)!!
                    }
                }

                field = value
            }

        private var currentController = NO_CONTROLLER
        private val buttonCodeMapping = DynIntArray(ButtonType.values().size, -1)
        private val hatCodeMapping = DynIntArray(ButtonType.values().size, -1)
        private val axisButtonMapping = DynIntArray(4, -1)
        private val pressedCodeMapping = BitSet(255)

        private val updateScheduler = FFContext.timer.createUpdateScheduler(30f)

        private var buttons: ByteBuffer? = null
        private var axis: FloatBuffer? = null
        private var hats: ByteBuffer? = null

        init {
            GLFW.glfwSetJoystickCallback(this::joystickCallback)
        }

        private fun joystickCallback(slot: Int, event: Int) {
            if (slot != this.slot)
                return

            if (event == GLFW.GLFW_CONNECTED || GLFW.glfwJoystickPresent(slot)) {
                currentController = ControllerDef(slot, GLFW.glfwGetJoystickName(slot)!!)
            } else {
                currentController = NO_CONTROLLER
                buttons = null
                axis = null
                hats = null
            }
        }

        fun update(): Boolean {
            if (currentController == NO_CONTROLLER)
                return false

            buttons = GLFW.glfwGetJoystickButtons(currentController.id)!!
            axis = GLFW.glfwGetJoystickAxes(currentController.id)!!
            hats = GLFW.glfwGetJoystickHats(currentController.id)!!

            return true
        }

        override fun buttonPressed(button: ButtonType): Boolean {
            if (currentController == NO_CONTROLLER || !GLFW.glfwJoystickPresent(currentController.id))
                    return false

            if (updateScheduler.needsUpdate())
                update()

            val buttonCode = button.ordinal
            if (!hatCodeMapping.isEmpty(buttonCode) && hats!!.capacity() > 0) {
                val keyCode = hatCodeMapping[buttonCode]
                if ((hats!![0] and keyCode.toByte() > 0))
                    return true
            }

            if (!axisButtonMapping.isEmpty) {
                val axisCode = axisButtonMapping.indexOf(buttonCode)
                if (axisCode == 0 && axis!![1] < -.5f)
                    return true
                if (axisCode == 1 && axis!![0] > .5f)
                    return true
                if (axisCode == 2 && axis!![1] > .5f)
                    return true
                if (axisCode == 3 && axis!![0] < -.5f)
                    return true
            }

            val keyCode = buttonCodeMapping[buttonCode]
            if (keyCode >= 0 && buttons!![keyCode] == ONE)
                return true

            return false
        }

        override fun buttonTyped(button: ButtonType): Boolean {
            val buttonCode = button.ordinal
            val pressed = buttonPressed(button)
            if (pressed && pressedCodeMapping[buttonCode])
                return false

            pressedCodeMapping[buttonCode] = pressed
            return pressed
        }

        override fun mapButtonInput(buttonType: ButtonType, padButton: Int) {
            buttonCodeMapping[buttonType.ordinal] = padButton
        }

        override fun mapHatInput(buttonType: ButtonType, padHat: Int) {
            hatCodeMapping[buttonType.ordinal] = padHat
        }

        override fun mapAxisButtonInput(buttonType: ButtonType, axisButton: Int) {
            when (axisButton) {
                GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP -> axisButtonMapping[0] = buttonType.ordinal
                GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT -> axisButtonMapping[1] = buttonType.ordinal
                GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN -> axisButtonMapping[2] = buttonType.ordinal
                GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT -> axisButtonMapping[3] = buttonType.ordinal
            }
        }

        override fun axis(axisType: Int): Float =
                GLFW.glfwGetJoystickAxes(currentController.id)!![axisType]

        companion object : InputImpl {
            private val NO_CONTROLLER = ControllerDef(-1, "NULL")
            private const val ONE = 1.toByte()
            override val type = DeviceType.JOYSTICK_PAD
            @Suppress("UNCHECKED_CAST")
            override fun <T : InputDevice> create(window: Long): T = GLFWControllerInput(window) as T
        }
    }


}