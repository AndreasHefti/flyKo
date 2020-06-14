package com.inari.firefly

import com.inari.firefly.external.*

object InputMock : FFInput {

    override
    val xpos: Int
        get() = 0

    override
    val ypos: Int
        get() = 0
    override val dx: Int
        get() = 0
    override val dy: Int
        get() = 0

    override val implementations: List<FFInput.InputImpl> = listOf()
    override val devices: MutableMap<String, FFInput.InputDevice>
        get() = TODO("Not yet implemented")

    override fun <T : FFInput.InputDevice> createDevice(name: String, implementation: FFInput.InputImpl, window: Long): T {
        TODO("Not yet implemented")
    }

    override fun getDevice(name: String): FFInput.InputDevice {
        TODO("Not yet implemented")
    }

    override fun createOrAdapter(name: String, a: String, b: String): FFInput.ORAdapter {
        TODO("Not yet implemented")
    }

    override fun clearDevice(name: String) {
        TODO("Not yet implemented")
    }

    override fun setKeyCallback(callback: KeyCallback) {
        TODO("Not yet implemented")
    }

    override fun setMouseButtonCallback(callback: MouseCallback) {
        TODO("Not yet implemented")
    }

    override fun setJoystickConnectionCallback(callback: JoystickConnectionCallback) {
        TODO("Not yet implemented")
    }

    override fun setButtonCallback(device: String, callback: ButtonCallback) {
        TODO("Not yet implemented")
    }

    override fun resetInputCallbacks() {
        TODO("Not yet implemented")
    }


}
