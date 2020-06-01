package com.inari.firefly

import com.inari.firefly.external.FFInput

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


}
