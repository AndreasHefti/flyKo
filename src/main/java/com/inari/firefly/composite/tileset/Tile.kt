package com.inari.firefly.composite.tileset

import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.sprite.ProtoSprite
import com.inari.firefly.physics.contact.ContactSystem
import com.inari.util.aspect.Aspect
import com.inari.util.collection.DynIntArray
import com.inari.util.geom.BitMask
import com.inari.util.graphics.RGBColor

@ComponentDSL
class Tile internal constructor() {

    @JvmField internal var spriteData: ProtoSprite = ProtoSprite()
    @JvmField internal var material: Aspect = ContactSystem.UNDEFINED_MATERIAL
    @JvmField internal var contactType: Aspect = ContactSystem.UNDEFINED_CONTACT_TYPE
    @JvmField internal var contactMask: BitMask? = null
    @JvmField internal var tintColor: RGBColor? = null
    @JvmField internal var blendMode: BlendMode? = null
    @JvmField internal var animation: TileAnimation? = null

    var ff_Sprite: ProtoSprite
        get() = spriteData
        set(value) {spriteData = value}

    var ff_Material: Aspect
        get() = material
        set(value) {material = value}

    var ff_ContactType: Aspect
        get() = contactType
        set(value) {contactType = value}

    var ff_ContactMask: BitMask
        get() = contactMask!!
        set(value) {contactMask = value}

    var ff_TintColor: RGBColor
        get() = tintColor!!
        set(value) {tintColor = value}

    var ff_BlendMode: BlendMode
        get() = blendMode!!
        set(value) {blendMode = value}

    val ff_withAnimation: (TileAnimation.() -> Unit) -> Unit = { configure ->
        val animationData = TileAnimation()
        animationData.also(configure)
        animation = animationData
    }

    val ff_withSprite: (ProtoSprite.() -> Unit) -> Unit = { configure ->
        val sprite = ProtoSprite()
        sprite.also(configure)
        this.spriteData = sprite
    }

    @JvmField internal val entityIds: DynIntArray = DynIntArray(5, -1, 5)

    val hasContactComp: Boolean
        get() = contactType !== ContactSystem.UNDEFINED_CONTACT_TYPE ||
                material !== ContactSystem.UNDEFINED_MATERIAL

    companion object {

        @JvmField internal val EMPTY_TILE = Tile()

        val of: (Tile.() -> Unit) -> Tile = { configure ->
            val comp = Tile()
            comp.also(configure)
            comp
        }
    }
}