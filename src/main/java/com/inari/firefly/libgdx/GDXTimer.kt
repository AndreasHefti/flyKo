package com.inari.firefly.libgdx

import com.badlogic.gdx.utils.TimeUtils
import com.inari.firefly.external.FFTimer

object GDXTimer : FFTimer() {
    override val tickAction: () -> Unit
        get() = {
            if (lastUpdateTime == 0L) {
                lastUpdateTime = TimeUtils.millis()
            } else {
                val currentTime = TimeUtils.millis()
                time += timeElapsed
                timeElapsed = currentTime - lastUpdateTime
                lastUpdateTime = currentTime
            }
        }
}