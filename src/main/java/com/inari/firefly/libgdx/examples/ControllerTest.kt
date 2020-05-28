package com.inari.firefly.libgdx.examples

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.controllers.Controllers
import com.inari.firefly.libgdx.GDXAppAdapter

class ControllerTest : GDXAppAdapter() {

    override val title: String = this.javaClass.simpleName

    override fun init() {
        println("Controller count: ${Controllers.getControllers()}")


    }

    override fun resize(width: Int, height: Int) {
        fitBaseViewportToScreen(width, height, 704, 480, true)
    }

}

fun main(args: Array<String>) {
    try {
        val config = Lwjgl3ApplicationConfiguration()
        config.setResizable(true)
        config.setWindowedMode(704, 480)
        Lwjgl3Application(ControllerTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}