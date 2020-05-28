package com.inari.firefly.libgdx.examples

import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
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
        val config = Lwjgl3ApplicationConfiguration()
        config.setResizable(true)
        config.setWindowedMode(704, 480)
        Lwjgl3Application(FPSInfoTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}