package com.inari.firefly.apptests

import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.inari.firefly.libgdx.GDXAppAdapter
import com.inari.firefly.system.FFInfoSystem
import com.inari.firefly.system.FrameRateInfo

class FPSInfoTest : GDXAppAdapter() {

    override val title: String = "FPSInfoTest"

    override fun init() {
        FFInfoSystem
            .addInfo(FrameRateInfo)
            .activate()

        addExitKeyTrigger(Input.Keys.SPACE)
    }

}

fun main(args: Array<String>) {
    try {
        val config = LwjglApplicationConfiguration()
        config.resizable = false
        config.width = 704
        config.height = 480
        config.fullscreen = false
        LwjglApplication(FPSInfoTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}