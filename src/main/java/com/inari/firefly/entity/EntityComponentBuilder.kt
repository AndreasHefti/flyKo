package com.inari.firefly.entity

import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentBuilder

abstract class EntityComponentBuilder<C : EntityComponent> : ComponentBuilder<C>() {
    private fun doBuild(comp: C, configure: C.() -> Unit, receiver: (C) -> C): CompId {
        comp.also(configure)
        receiver(comp)
        return comp.componentId
    }
    fun builder(receiver: (C) -> C): (C.() -> Unit) -> CompId = {
        configure -> doBuild(createEmpty(), configure, receiver)
    }
    override abstract public fun createEmpty(): C
}