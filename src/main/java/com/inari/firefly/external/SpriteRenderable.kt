
package com.inari.firefly.external

import com.inari.commons.graphics.RGBColor
import com.inari.firefly.graphics.BlendMode

interface SpriteRenderable {
    val spriteId: Int
    val tintColor: RGBColor
    val blendMode: BlendMode
    val shaderId: Int
}
