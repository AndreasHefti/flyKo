package com.inari.firefly.physics.animation.entity

import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.Controller
import com.inari.firefly.physics.animation.AnimationSystem
import com.inari.firefly.entity.EntityComponent
import java.util.*

class EAnimation : EntityComponent() {

    @JvmField internal var controllerRef = -1
    @JvmField internal val animations: BitSet = BitSet()
    @JvmField internal val activeAnimations: BitSet = BitSet()

    val ff_Controller =
        ComponentRefResolver(Controller, { index->
            controllerRef = setIfNotInitialized(index, "ff_Controller")
        })

    fun <A : EntityPropertyAnimation> with(cBuilder: EntityPropertyAnimation.Builder<A>, configure: (A.() -> Unit)): A {
        val animation = cBuilder.doBuild(configure)
        animations.set(animation.index())
        return animation
    }

    fun <A : EntityPropertyAnimation> withActive(cBuilder: EntityPropertyAnimation.Builder<A>, configure: (A.() -> Unit)): A {
        val animation = cBuilder.doBuild(configure)
        animations.set(animation.index())
        activeAnimations.set(animation.index())
        return animation
    }

    override fun reset() {
        var i = animations.nextSetBit(0)
        while (i >= 0) {
            AnimationSystem.animations.delete(i)
            i = animations.nextSetBit(i + 1)
        }
        animations.clear()
        activeAnimations.clear()
    }

    override fun indexedTypeKey() = typeKey
    companion object : EntityComponentType<EAnimation>() {
        override val typeKey = EntityComponent.createTypeKey(EAnimation::class.java)
        override fun createEmpty() = EAnimation()
    }
}