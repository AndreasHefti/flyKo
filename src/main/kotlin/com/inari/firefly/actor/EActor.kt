package com.inari.firefly.actor

import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.physics.contact.ContactConstraint
import com.inari.firefly.physics.contact.ContactSystem
import com.inari.firefly.physics.contact.EContact
import com.inari.util.IntPredicate
import com.inari.util.aspect.Aspect
import com.inari.util.aspect.IndexedAspectType

class EActor private constructor () : EntityComponent(EActor::class.java.name) {

    @JvmField internal var encounterContactConstraintRef = -1
    @JvmField internal var hitContactConstraintRef = -1

    var category: Aspect = UNDEFINED_ACTOR_CATEGORY
        set(value) { if (ACTOR_CATEGORY_ASPECT.typeCheck(value)) field = value else throw IllegalArgumentException() }
    var type: Aspect = UNDEFINED_ACTOR_TYPE
        set(value) { if (ACTOR_TYPE_ASPECT.typeCheck(value)) field = value else throw IllegalArgumentException() }
    var health: Int = -1
    var maxHealth: Int = -1
    var hitPower: Int = 0
    val encounterConstraint = ComponentRefResolver(ContactConstraint) {
            index -> encounterContactConstraintRef = index
        }
    val hitConstraint = ComponentRefResolver(ContactConstraint) {
            index -> hitContactConstraintRef = index
        }

    override fun reset() {
        category = UNDEFINED_ACTOR_CATEGORY
        type = UNDEFINED_ACTOR_TYPE
        maxHealth = -1
        health = -1
        encounterContactConstraintRef = -1
        hitContactConstraintRef = -1
    }

    override fun componentType() = Companion

    companion object : EntityComponentType<EActor>(EActor::class.java) {

        @JvmField val ACTOR_CATEGORY_ASPECT = IndexedAspectType("ACTOR_CATEGORY_ASPECT")
        @JvmField val UNDEFINED_ACTOR_CATEGORY = ACTOR_CATEGORY_ASPECT.createAspect("UNDEFINED_ACTOR_CATEGORY")

        @JvmField val ACTOR_TYPE_ASPECT = IndexedAspectType("ACTOR_TYPE_ASPECT")
        @JvmField val UNDEFINED_ACTOR_TYPE = ACTOR_TYPE_ASPECT.createAspect("UNDEFINED_ACTOR_TYPE")

        @JvmField val ENCOUNTER_CONTACT_TYPE =
                ContactSystem.CONTACT_TYPE_ASPECT_GROUP.createAspect("ENCOUNTER_CONTACT_TYPE")

        @JvmField val ANY_ENCOUNTER_CONDITION: IntPredicate = object : IntPredicate {
            override fun invoke(entityId: Int): Boolean {
                val entity = EntitySystem[entityId]
                val actor = entity[EActor]
                return if (actor.encounterContactConstraintRef < 0) false
                    else !entity[EContact].contactScan[actor.encounterContactConstraintRef].hasAnyContact()
            }
        }

        @JvmField val ANY_HIT_CONDITION: IntPredicate = object : IntPredicate {
            override fun invoke(entityId: Int): Boolean {
                val entity = EntitySystem[entityId]
                val actor = entity[EActor]
                return if (actor.hitContactConstraintRef < 0) false
                    else !entity[EContact].contactScan[actor.hitContactConstraintRef].hasAnyContact()
            }
        }

        override fun createEmpty() = EActor()
    }
}