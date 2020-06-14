package com.inari.firefly.libgdx

import com.badlogic.gdx.Gdx
import com.inari.firefly.FFApp
import com.inari.util.event.EventDispatcher

object DesktopApp : FFApp(
    { EventDispatcher() },
    { GDXGraphics },
    { GDXAudio },
    { DesktopInput },
    { GDXTimer }
) {

    fun exit() {
        Gdx.app.exit()
    }
}