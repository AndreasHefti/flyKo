package com.inari.firefly.physics.contact

import com.inari.firefly.Named
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.firefly.physics.contact.ContactSystem.CONTACT_TYPE_ASPECT_GROUP
import com.inari.firefly.physics.contact.ContactSystem.MATERIAL_ASPECT_GROUP
import com.inari.util.aspect.Aspect
import com.inari.util.geom.BitMask
import com.inari.util.geom.Rectangle
import com.inari.util.indexed.Indexed

class EContact private constructor() : EntityComponent(EContact::class.java.name) {

    @JvmField internal var resolverRef = -1
    @JvmField internal val contactScan = ContactScan()

    val collisionResolver = ComponentRefResolver(CollisionResolver) { index -> resolverRef = index }
    var bounds: Rectangle = Rectangle()
    var mask: BitMask = BitMask(width = 0, height = 0)
        set(value) {
            mask.reset(value.region())
            mask.or(value)
        }
    var material: Aspect  = ContactSystem.UNDEFINED_MATERIAL
        set(value) =
            if (MATERIAL_ASPECT_GROUP.typeCheck(value)) field = value
            else throw IllegalArgumentException()

    var contactType: Aspect  = ContactSystem.UNDEFINED_CONTACT_TYPE
        set(value) =
            if (CONTACT_TYPE_ASPECT_GROUP.typeCheck(value)) field = value
            else throw IllegalArgumentException()

    val addConstraint =
        ComponentRefResolver(ContactConstraint) { id ->
            if (id !in contactScan.contacts) contactScan.contacts[id] = Contacts(id)
        }

    val removeConstraint =
        ComponentRefResolver(ContactConstraint) { id: Int ->
            contactScan.contacts.remove(id)
        }

    val constraint: (ContactConstraint.() -> Unit) -> Unit = { configure ->
        val id = ContactConstraint.build(configure)
        contactScan.contacts[id.instanceId] = Contacts(id.instanceId)
    }

    fun clearConstraints() =
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

    override fun componentType() = Companion
    companion object : EntityComponentType<EContact>(EContact::class.java) {
        override fun createEmpty() = EContact()
    }
}