package com.inari.firefly.graphics.tile.set

import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.sprite.ProtoSprite
import com.inari.firefly.graphics.tile.set.TileSetContext.EMPTY_PROTO_SPRITE
import com.inari.firefly.graphics.tile.set.TileSetContext.EMPTY_TILE_ASPECTS
import com.inari.firefly.physics.contact.ContactSystem
import com.inari.util.aspect.Aspect
import com.inari.util.aspect.Aspects
import com.inari.util.geom.BitMask
import com.inari.util.graphics.RGBColor

@ComponentDSL
class ProtoTile internal constructor() {

    @JvmField internal var spriteData: ProtoSprite = EMPTY_PROTO_SPRITE
    @JvmField internal var int_animation: TileAnimation? = null
    @JvmField internal var entityRef = -1

    var aspects: Aspects = EMPTY_TILE_ASPECTS
    var material: Aspect = ContactSystem.UNDEFINED_MATERIAL
    var contactType: Aspect = ContactSystem.UNDEFINED_CONTACT_TYPE
    var contactMask: BitMask? = null
    var tintColor: RGBColor? = null
    var blendMode: BlendMode? = null
    val animation: (TileAnimation.() -> Unit) -> Unit = { configure ->
        val animationData = TileAnimation.of {}
        animationData.also(configure)
        int_animation = animationData
    }
    val sprite: (ProtoSprite.() -> Unit) -> Unit = { configure ->
        val sprite = ProtoSprite.of {}
        sprite.also(configure)
        this.spriteData = sprite
    }

    val hasContactComp: Boolean
        get() = contactType !== ContactSystem.UNDEFINED_CONTACT_TYPE ||
                material !== ContactSystem.UNDEFINED_MATERIAL

    companion object {
        val of: (ProtoTile.() -> Unit) -> ProtoTile = { configure ->
            val comp = ProtoTile()
            comp.also(configure)
            comp
        }
    }
}