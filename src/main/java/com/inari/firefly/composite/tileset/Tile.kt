package com.inari.firefly.composite.tileset

import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.sprite.Sprite
import com.inari.firefly.physics.contact.ContactSystem
import com.inari.util.aspect.Aspect
import com.inari.util.collection.DynIntArray
import com.inari.util.geom.BitMask
import com.inari.util.graphics.RGBColor

@ComponentDSL
class Tile internal constructor() {

    @JvmField internal var spriteData: Sprite = Sprite.NULL_SPRITE
    var ff_Sprite: Sprite
        get() = spriteData
        set(value) {spriteData = value}

    @JvmField internal var material: Aspect = ContactSystem.UNDEFINED_MATERIAL
    var ff_Material: Aspect
        get() = material
        set(value) {material = value}

    @JvmField internal var contactType: Aspect = ContactSystem.UNDEFINED_CONTACT_TYPE
    var ff_ContactType: Aspect
        get() = contactType
        set(value) {contactType = value}

    @JvmField internal var contactMask: BitMask? = null
    var ff_ContactMask: BitMask
        get() = contactMask!!
        set(value) {contactMask = value}

    @JvmField internal var tintColor: RGBColor? = null
    var ff_TintColor: RGBColor
        get() = tintColor!!
        set(value) {tintColor = value}

    @JvmField internal var blendMode: BlendMode? = null
    var ff_BlendMode: BlendMode
        get() = blendMode!!
        set(value) {blendMode = value}

    @JvmField internal var animation: AnimationData? = null
    val ff_withAnimation: (AnimationData.() -> Unit) -> Unit = { configure ->
        val animationData = AnimationData()
        animationData.also(configure)
        animation = animationData
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