
package com.inari.firefly.external

import com.inari.firefly.graphics.BlendMode
import com.inari.util.graphics.RGBColor

class SpriteRenderable(
    @JvmField var spriteId: Int = -1,
    @JvmField var tintColor: RGBColor = RGBColor(1f, 1f, 1f, 1f),
    @JvmField var blendMode: BlendMode = BlendMode.NONE,
    @JvmField var shaderId: Int = -1
) {

    fun reset() {
        spriteId = -1
        tintColor = RGBColor(1f, 1f, 1f, 1f)
        blendMode = BlendMode.NONE
        shaderId = -1
    }

    override fun toString(): String =
        "SpriteRenderable(spriteId=$spriteId, tintColor=$tintColor, blendMode=$blendMode, shaderId=$shaderId)"

}
