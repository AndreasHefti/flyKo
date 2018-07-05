package com.inari.firefly.physics.contact

import com.inari.util.aspect.Aspect
import com.inari.util.geom.BitMask
import com.inari.util.geom.GeomUtils
import com.inari.util.geom.Rectangle


class Contact internal constructor() {

    @JvmField internal var entity = -1
    @JvmField internal val bounds = Rectangle()
    @JvmField internal val intersection = Rectangle()
    @JvmField internal val mask = BitMask(width = 0, height = 0)
    @JvmField internal var contact = ContactSystem.UNDEFINED_CONTACT_TYPE
    @JvmField internal var material = ContactSystem.UNDEFINED_MATERIAL

    val entityId: Int
        get() = entity

    val worldBounds: Rectangle
        get() = bounds

    val intersectionBounds: Rectangle
        get() = intersection

    val intersectionMask: BitMask
        get() = mask

    val contactType: Aspect
        get() = contact

    val materialType: Aspect
        get() = material

    fun intersects(x: Int, y: Int): Boolean =
        GeomUtils.contains(intersection, x, y)

    fun hasContact(x: Int, y: Int): Boolean {
        if (GeomUtils.contains(intersection, x, y)) {
            if (!mask.isEmpty) {
                val region = mask.region()
                return mask.getBit(x - region.pos.x, y - region.pos.y)
            }
            return true
        }
        return false
    }

    override fun toString(): String {
        return "Contact(entity=$entity, " +
            "bounds=$bounds, " +
            "intersection=$intersection, " +
            "mask=$mask, " +
            "contact=$contact, " +
            "material=$material)"
    }

    companion object {
        @JvmField val NO_CONTACT = Contact()
    }


}