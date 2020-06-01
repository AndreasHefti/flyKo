/*******************************************************************************
 * Copyright (c) 2015 - 2016, Andreas Hefti, inarisoft@yahoo.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inari.firefly.external

import com.inari.firefly.libgdx.GDXInput
import java.lang.UnsupportedOperationException

interface FFInput {

    val xpos: Int
    val ypos: Int
    val dx: Int
    val dy: Int

    enum class DeviceType {
        KEYBOARD,
        MOUSE,
        JOYSTICK_PAD,
        TOUCH,
        OR
    }

    enum class ButtonType {
        UP,
        RIGHT,
        DOWN,
        LEFT,

        ENTER,
        QUIT,
        FIRE_1,
        FIRE_2,

        BUTTON_A,
        BUTTON_B,
        BUTTON_C,
        BUTTON_D,

        BUTTON_W,
        BUTTON_X,
        BUTTON_Y,
        BUTTON_Z,

        BUTTON_0,
        BUTTON_1,
        BUTTON_2,
        BUTTON_3,
        BUTTON_4,
        BUTTON_5,
        BUTTON_6,
        BUTTON_7,
        BUTTON_8,
        BUTTON_9
    }

    interface InputImpl {
        val type: DeviceType
        fun <T : InputDevice> create(window: Long = 0): T
    }

    val implementations: List<InputImpl>
    val devices: MutableMap<String, InputDevice>

    fun <T : InputDevice> createDevice(name: String, implementation: InputImpl, window: Long = -1): T
    fun getDevice(name: String): InputDevice
    fun <T : InputDevice> getDeviceOf(name: String): T =
            GDXInput.getDevice(name) as T
    fun createOrAdapter(name: String, a: String, b: String): ORAdapter {
        val adapter = ORAdapter(getDevice(a), getDevice(b), name)
        devices[name] = adapter
        return adapter
    }


    interface InputDevice {

        val name: String
        val type: InputImpl
        val window: Long

        fun buttonTyped(button: ButtonType): Boolean
        fun buttonPressed(button: ButtonType): Boolean

    }

    interface KeyInput : InputDevice {
         fun mapKeyInput(buttonType: ButtonType, keyCode: Int)
    }

    interface MouseInput : InputDevice {
        val xPosition: Float
        val yPosition: Float
        val xScroll: Float
        val yScroll: Float
        fun mapButtonInput(buttonType: ButtonType, mouseButton: Int)
    }

    interface ControllerInput : InputDevice {
        val controllerDefinitions: List<ControllerDef>
        var controller: ControllerDef
        fun axis(axisType: Int): Float
        fun mapButtonInput(buttonType: ButtonType, padButton: Int)
        fun mapHatInput(buttonType: ButtonType, padHat: Int)
        data class ControllerDef(
                val id: Int,
                val name: String
        )
    }

    class ORAdapter(
            val a: InputDevice,
            val b: InputDevice,
            override val name: String = "OR",
            override val window: Long = -1
    ) : InputDevice {

        override val type: InputImpl = Companion
        override fun buttonTyped(button: ButtonType): Boolean =
                a.buttonTyped(button) || b.buttonTyped(button)
        override fun buttonPressed(button: ButtonType): Boolean =
                a.buttonPressed(button) || b.buttonPressed(button)
        companion object : InputImpl{
            override val type = DeviceType.OR
            override fun <T : InputDevice> create(window: Long): T =
                    throw UnsupportedOperationException()
        }
    }

    companion object {
        val NULL_INPUT_DEVICE: InputDevice = object : InputDevice {
            override val name: String = "NULL"
            override val type: InputImpl = object : InputImpl {
                override val type: DeviceType
                    get() = TODO("Not yet implemented")
                override fun <T : InputDevice> create(window: Long): T {
                    TODO("Not yet implemented")
                }
            }
            override val window: Long = 0
            override fun buttonTyped(button: ButtonType): Boolean = false
            override fun buttonPressed(button: ButtonType): Boolean = false
        }
    }

}
