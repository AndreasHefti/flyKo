package com.inari.firefly.graphics.tile.set

import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.sprite.ProtoSprite
import com.inari.firefly.physics.contact.ContactSystem
import com.inari.util.aspect.Aspect
import com.inari.util.collection.DynIntArray
import com.inari.util.geom.BitMask
import com.inari.util.graphics.RGBColor

@ComponentDSL
class ProtoTile internal constructor() {

    @JvmField internal var spriteData: ProtoSprite = ProtoSprite()
    @JvmField internal var int_animation: TileAnimation? = null

    var sprite: ProtoSprite  = ProtoSprite()
    var material: Aspect = ContactSystem.UNDEFINED_MATERIAL
    var contactType: Aspect = ContactSystem.UNDEFINED_CONTACT_TYPE
    var contactMask: BitMask? = null
    var tintColor: RGBColor? = null
    var blendMode: BlendMode? = null
    val animation: (TileAnimation.() -> Unit) -> Unit = { configure ->
        val animationData = TileAnimation()
        animationData.also(configure)
        int_animation = animationData
    }
    val protoSprite: (ProtoSprite.() -> Unit) -> Unit = { configure ->
        val sprite = ProtoSprite()
        sprite.also(configure)
        this.spriteData = sprite
    }

    @JvmField internal val entityIds: DynIntArray = DynIntArray(5, -1, 5)

    val hasContactComp: Boolean
        get() = contactType !== ContactSystem.UNDEFINED_CONTACT_TYPE ||
                material !== ContactSystem.UNDEFINED_MATERIAL

    companion object {

        @JvmField internal val EMPTY_TILE = ProtoTile()

        val of: (ProtoTile.() -> Unit) -> ProtoTile = { configure ->
            val comp = ProtoTile()
            comp.also(configure)
            comp
        }
    }
}