package com.inari.firefly.physics.animation.entity

import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.firefly.physics.animation.Animation
import com.inari.firefly.physics.animation.AnimationSystem
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.java.types.BitSet

class EAnimation : EntityComponent(EAnimation::class.java.name) {

    @JvmField internal val animations: BitSet = BitSet()

    val ff_WithAnimation = ComponentRefResolver(Animation) { index->
        if (initialized)
            throw IllegalStateException("EAnimation instance is already created")
        animations.set(index)
    }

    fun <A : Animation> ff_WithAnimation(builder: SystemComponentSubType<Animation, A>, configure: (A.() -> Unit)): CompId {
        if (initialized)
            throw IllegalStateException("EAnimation instance is already created")
        val id = builder.build(configure)
        animations.set(id.index)
        return id
    }

    fun <A : Animation> ff_WithActiveAnimation(builder: SystemComponentSubType<Animation, A>, configure: (A.() -> Unit)): CompId {
        if (initialized)
            throw IllegalStateException("EAnimation instance is already created")
        val id = builder.buildAndActivate(configure)
        animations.set(id.index)
        return id
    }

    override fun reset() {
        animations.clear()
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<EAnimation>(EAnimation::class.java) {
        override fun createEmpty() = EAnimation()
    }
}