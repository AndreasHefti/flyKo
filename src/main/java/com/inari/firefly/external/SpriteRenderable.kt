
package com.inari.firefly.external

import com.inari.firefly.graphics.BlendMode
import com.inari.util.graphics.RGBColor

interface SpriteRenderable {
    val spriteId: Int
    val tintColor: RGBColor
    val blendMode: BlendMode
    val shaderId: Int
}
