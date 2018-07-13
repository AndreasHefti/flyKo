package com.inari.firefly.physics.contact

import com.inari.firefly.Named
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.util.aspect.Aspect
import com.inari.util.geom.BitMask
import com.inari.util.geom.Rectangle
import com.inari.util.indexed.Indexed

class EContact private constructor() : EntityComponent() {

    @JvmField internal var resolverRef = -1
    @JvmField internal val bounds = Rectangle()
    @JvmField internal val mask = BitMask(width = 0, height = 0)
    @JvmField internal var material = ContactSystem.UNDEFINED_MATERIAL
    @JvmField internal var contactType = ContactSystem.UNDEFINED_CONTACT_TYPE

    @JvmField internal val contactScan = ContactScan()

    val ff_CollisionResolver = ComponentRefResolver(CollisionResolver) { index -> resolverRef = index }
    var ff_Bounds: Rectangle
        get() = bounds
        set(value) = bounds(value)
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
        ComponentRefResolver(ContactConstraint) { id ->
            if (id !in contactScan.contacts) contactScan.contacts.set(id, Contacts(id))
        }
    val ff_removeConstraint =
        ComponentRefResolver(ContactConstraint) { id: Int ->
            contactScan.contacts.remove(id)
        }
    fun ff_clearConstraints() =
        contactScan.clear()

    fun contacts(constraint: ContactConstraint): Contacts =
        contacts(constraint.index)

    fun contacts(constraint: CompId): Contacts =
        contacts(constraint.instanceId)

    fun contacts(constraint: Named): Contacts =
        contacts(constraint.name)

    fun contacts(constraint: String): Contacts =
        contacts(ContactSystem.constraints[constraint].index)

    fun contacts(constraint: Indexed): Contacts =
        contacts(constraint.index)

    fun contacts(constraint: Int): Contacts =
        contactScan[constraint]


    override fun reset() {
        resolverRef = -1
        bounds(0, 0, 0, 0)
        mask.clearMask()
        material = ContactSystem.UNDEFINED_MATERIAL
        contactType = ContactSystem.UNDEFINED_CONTACT_TYPE
        contactScan.clear()
    }

    override fun componentType() =
        EContact.Companion

    companion object : EntityComponentType<EContact>(EContact::class.java) {
        override fun createEmpty() = EContact()
    }
}