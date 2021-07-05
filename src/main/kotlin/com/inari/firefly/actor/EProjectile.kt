package com.inari.firefly.actor

import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.firefly.physics.contact.ContactSystem
import com.inari.util.aspect.Aspect
import com.inari.util.aspect.IndexedAspectType

class EProjectile private constructor () : EntityComponent(EProjectile::class.simpleName!!) {

    var type: Aspect = UNDEFINED_PROJECTILE_TYPE
        set(value) =
            if (PROJECTILE_TYPE_ASPECT.typeCheck(value)) field = value
            else throw IllegalArgumentException()
    var hitPower: Int = 0

    override fun reset() {
        type = UNDEFINED_PROJECTILE_TYPE
        hitPower = 0
    }

    override fun componentType() = Companion

    companion object : EntityComponentType<EProjectile>(EProjectile::class) {

        @JvmField val PROJECTILE_TYPE_ASPECT = IndexedAspectType("PROJECTILE_TYPE_ASPECT")
        @JvmField val UNDEFINED_PROJECTILE_TYPE = PROJECTILE_TYPE_ASPECT.createAspect("UNDEFINED_PROJECTILE_TYPE")

        @JvmField val PROJECTILE_CONTACT_TYPE =
                ContactSystem.CONTACT_TYPE_ASPECT_GROUP.createAspect("PROJECTILE_CONTACT_TYPE")

        override fun createEmpty() = EProjectile()
    }
}