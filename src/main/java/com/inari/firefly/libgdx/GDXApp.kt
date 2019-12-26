package com.inari.firefly.libgdx

import com.badlogic.gdx.Gdx
import com.inari.firefly.FFApp
import com.inari.util.event.EventDispatcher

object GDXApp : FFApp(
    { EventDispatcher() },
    { GDXGraphics },
    { GDXAudio },
    { GDXInput },
    { GDXTimer }
) {

    fun exit() {
        Gdx.app.exit()
    }
}