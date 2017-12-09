package com.inari.firefly.physics.animation

import com.inari.commons.lang.list.DynArray
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.Controller
import com.inari.firefly.entity.EntityComponent

class EAnimation : EntityComponent() {

    @JvmField internal var controllerRef = -1
    @JvmField internal val animations: DynArray<AnimatedProperty> =
        DynArray.createTyped(AnimatedProperty::class.java)

    val ff_Controller =
        ComponentRefResolver(Controller, { index->
            controllerRef = setIfNotInitialized(index, "ff_Controller")
        })

    fun <A : AnimatedProperty> withAnimated(cBuilder: AnimatedProperty.Builder<A>, configure: (A.() -> Unit)): A =
        cBuilder.builder( { comp ->
            animations.add(comp)
            comp
        } )(configure)

    override fun reset() {
        animations.clear()
    }

    override fun indexedTypeKey() = typeKey
    companion object : EntityComponentType<EAnimation>() {
        override val typeKey = EntityComponent.createTypeKey(EAnimation::class.java)
        override fun createEmpty() = EAnimation()
    }
}