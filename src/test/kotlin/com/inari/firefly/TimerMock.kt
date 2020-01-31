package com.inari.firefly

import com.inari.firefly.external.FFTimer

object TestTimer : FFTimer() {

    override val tickAction = {
        lastUpdateTime++
        time++
        timeElapsed++
        Unit
    }

}
