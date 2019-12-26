package com.inari.firefly.libgdx

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration


class IntroTest : GDXAppAdapter() {

    override val title: String = "IntroTest"

    override fun init() {
        dispose()
        GDXApp.exit()
    }

}

fun main(args: Array<String>) {
    try {
        val config = LwjglApplicationConfiguration()
        config.resizable = false
        config.width = 704
        config.height = 480
        config.fullscreen = false
        LwjglApplication(IntroTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}