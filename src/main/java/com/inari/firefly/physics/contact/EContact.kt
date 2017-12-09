package com.inari.firefly.physics.contact

import com.inari.commons.geom.BitMask
import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.aspect.Aspect
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.entity.EntityComponent

class EContact private constructor() : EntityComponent() {

    @JvmField internal var resolverRef = -1
    @JvmField internal val bounds = Rectangle()
    @JvmField internal var mask = BitMask(0, 0)
    @JvmField internal var material = ContactSystem.UNDEFINED_MATERIAL
    @JvmField internal var contactType = ContactSystem.UNDEFINED_CONTACT_TYPE

    @JvmField internal val contactScan = ContactScan()

    val ff_CollisionResolver =
        ComponentRefResolver(CollisionResolver, { index ->
            resolverRef = setIfNotInitialized(index, "ff_CollisionResolver")
        })
    var ff_Bounds: Rectangle
        get() = bounds
        set(value) = bounds.setFrom(value)
    var ff_Mask: BitMask
        get() = mask
        set(value) {
            mask.reset(value.region())
            mask.or(value)
        }
    var ff_Material: Aspect
        get() = material
        set(value) {material = value}
    var ff_ContactType: Aspect
        get() = contactType
        set(value) {contactType = value}
    val ff_addConstraint =
        ComponentRefResolver(ContactConstraint, { id ->
            if (id !in contactScan.contacts) contactScan.contacts.set(id, Contacts(id))
        })
    val ff_removeConstraint =
        ComponentRefResolver(ContactConstraint, { id ->
            contactScan.contacts.remove(id)?.clear()
        })


    override fun reset() {
        resolverRef = -1
        bounds.clear()
        mask.clearMask()
        material = ContactSystem.UNDEFINED_MATERIAL
        contactType = ContactSystem.UNDEFINED_CONTACT_TYPE
        contactScan.clear()
    }

    override fun indexedTypeKey() = typeKey
    companion object : EntityComponentType<EContact>() {
        override val typeKey = EntityComponent.createTypeKey(EContact::class.java)
        override fun createEmpty() = EContact()
    }
}