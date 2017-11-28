package com.inari.firefly.external

import com.inari.commons.geom.Rectangle

interface SpriteData {
    val textureId: Int
    val textureRegion: Rectangle
    val isHorizontalFlip: Boolean
    val isVerticalFlip: Boolean
}
