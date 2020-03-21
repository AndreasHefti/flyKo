package com.inari.firefly.physics.animation.entity

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

    val ff_WithAnimation = ComponentRefResolver(Animation) {
        index-> animations.set(index)
    }

    fun <A : Animation> ff_WithAnimation(builder: SystemComponentSubType<Animation, A>, configure: (A.() -> Unit)): CompId {
        val id = builder.build(configure)
        animations.set(id.index)
        return id
    }


    fun <A : Animation> ff_WithActiveAnimation(builder: SystemComponentSubType<Animation, A>, configure: (A.() -> Unit)): CompId {
        val id = builder.buildAndActivate(configure)
        animations.set(id.index)
        return id
    }

    fun clearAnimations() {
        var i = animations.nextSetBit(0)
        while (i >= 0) {
            AnimationSystem.animations.delete(i)
            i = animations.nextSetBit(i + 1)
        }
        animations.clear()
    }

    override fun reset() {
        clearAnimations()
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<EAnimation>(EAnimation::class.java) {
        override fun createEmpty() = EAnimation()
    }
}