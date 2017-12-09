package com.inari.firefly.physics.contact

import com.inari.commons.lang.aspect.Aspect
import com.inari.commons.lang.list.DynArray
import com.inari.firefly.component.CompId


class ContactScan internal constructor() {

    @JvmField internal val contacts: DynArray<Contacts> =
        DynArray.create(Contacts::class.java)

    fun hasAnyContact(): Boolean {
        var i = 0
        while (i < contacts.capacity()) {
            val c = contacts.get(i++) ?: continue
            if (c.hasAnyContact())
                return true
        }
        return false
    }

    fun hasContact(contact: Aspect): Boolean {
        var i = 0
        while (i < contacts.capacity()) {
            val c = contacts.get(i++) ?: continue
            if (c.hasContact(contact))
                return true
        }
        return false
    }

    operator fun get(constraint: ContactConstraint): Contacts =
        get(constraint.index())

    operator fun get(constraint: CompId): Contacts =
        get(constraint.index)

    operator fun get(constraint: Int): Contacts =
        contacts.get(constraint)

    fun clearContacts() {
        var i = 0
        while (i < contacts.capacity())
            contacts.get(i++)?.clear()
    }

    internal fun clear() {
        clearContacts()
        contacts.clear()
    }
}