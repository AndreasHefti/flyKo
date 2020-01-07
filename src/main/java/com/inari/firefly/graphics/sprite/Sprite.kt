package com.inari.firefly.graphics.sprite

import com.inari.firefly.NO_NAME

class Sprite(
    @JvmField val x: Int,
    @JvmField val y: Int,
    @JvmField val width: Int,
    @JvmField val height: Int,
    @JvmField val flipH: Boolean = false,
    @JvmField val flipV: Boolean = false,
    @JvmField val name: String = NO_NAME
) {

    @JvmField internal var instId = -1
    val instanceId: Int get() = instId

    companion object {
        @JvmField val NULL_SPRITE = Sprite(0, 0, 0, 0)
    }
}