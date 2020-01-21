package com.inari.firefly.actor

import com.inari.firefly.IntPredicate
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.physics.contact.ContactConstraint
import com.inari.firefly.physics.contact.ContactSystem
import com.inari.firefly.physics.contact.EContact
import com.inari.util.aspect.Aspect
import com.inari.util.aspect.IndexedAspectType

class EActor private constructor () : EntityComponent(EActor::class.java.name) {

    @JvmField internal var category: Aspect = UNDEFINED_ACTOR_CATEGORY
    @JvmField internal var type: Aspect = UNDEFINED_ACTOR_TYPE

    @JvmField internal var maxHealth = -1
    @JvmField internal var health = -1

    @JvmField internal var hitPower = 0

    @JvmField internal var encounterContactConstraintRef = -1
    @JvmField internal var hitContactConstraintRef = -1

    var ff_Category: Aspect
        get() = category
        set(value) { if (ACTOR_CATEGORY_ASPECT.typeCheck(value)) category = value else throw IllegalArgumentException() }

    var ff_Type: Aspect
        get() = type
        set(value) { if (ACTOR_TYPE_ASPECT.typeCheck(value)) type = value else throw IllegalArgumentException() }

    var ff_Health: Int
        get() = health
        set(value) { health = value }

    var ff_MaxHealth: Int
        get() = maxHealth
        set(value) { maxHealth = value }

    var ff_HitPower: Int
        get() = hitPower
        set(value) { hitPower = value }

    val ff_EncounterConstraint = ComponentRefResolver(ContactConstraint) {
            index -> encounterContactConstraintRef = index
        }

    val ff_HitConstraint = ComponentRefResolver(ContactConstraint) {
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