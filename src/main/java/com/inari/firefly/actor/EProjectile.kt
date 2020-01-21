package com.inari.firefly.actor

import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.firefly.physics.contact.ContactSystem
import com.inari.util.aspect.Aspect
import com.inari.util.aspect.IndexedAspectType

class EProjectile private constructor () : EntityComponent(EProjectile::class.java.name) {

    @JvmField internal var type: Aspect = UNDEFINED_PROJECTILE_TYPE
    @JvmField internal var hitPower = 0

    var ff_Type: Aspect
        get() = type
        set(value) =
            if (PROJECTILE_TYPE_ASPECT.typeCheck(value)) type = value
            else throw IllegalArgumentException()

    var ff_HitPower: Int
        get() = hitPower
        set(value) { hitPower = value }

    override fun reset() {
        type = UNDEFINED_PROJECTILE_TYPE
        hitPower = 0
    }

    override fun componentType() = Companion

    companion object : EntityComponentType<EProjectile>(EProjectile::class.java) {

        @JvmField val PROJECTILE_TYPE_ASPECT = IndexedAspectType("PROJECTILE_TYPE_ASPECT")
        @JvmField val UNDEFINED_PROJECTILE_TYPE = PROJECTILE_TYPE_ASPECT.createAspect("UNDEFINED_PROJECTILE_TYPE")

        @JvmField val PROJECTILE_CONTACT_TYPE =
                ContactSystem.CONTACT_TYPE_ASPECT_GROUP.createAspect("PROJECTILE_CONTACT_TYPE")

        override fun createEmpty() = EProjectile()
    }
}