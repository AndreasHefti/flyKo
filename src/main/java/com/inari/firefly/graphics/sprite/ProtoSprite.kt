package com.inari.firefly.graphics.sprite

import com.inari.firefly.NO_NAME
import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.graphics.particle.SpriteParticle
import com.inari.util.geom.Rectangle

@ComponentDSL
class ProtoSprite internal constructor() {

    @JvmField internal val textureBounds: Rectangle = Rectangle()
    @JvmField internal var flipH: Boolean = false
    @JvmField internal var flipV: Boolean = false
    @JvmField internal var name: String = NO_NAME

    @JvmField internal var instId = -1
    val instanceId: Int get() = instId

    var ff_Name: String
        get() = name
        set(value) { name = value }

    val ff_textureBounds: Rectangle
        get() = textureBounds

    var ff_HFlip: Boolean
        get() = flipH
        set(value) { flipH = value }

    var ff_VFlip: Boolean
        get() = flipV
        set(value) { flipV = value }

    companion object {

        val of: (ProtoSprite.() -> Unit) -> ProtoSprite = { configure ->
            val instance = ProtoSprite()
            instance.also(configure)
            instance
        }
    }

}