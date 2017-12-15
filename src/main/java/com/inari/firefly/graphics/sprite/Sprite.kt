package com.inari.firefly.graphics.sprite

import com.inari.commons.geom.Rectangle
import com.inari.firefly.NO_NAME

class Sprite(
    @JvmField val name: String = NO_NAME,
    @JvmField val textureRegion: Rectangle,
    @JvmField val flipHorizontal: Boolean = false,
    @JvmField val flipVertical: Boolean = false
)