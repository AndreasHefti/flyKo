package com.inari.firefly.system.component

import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentBuilder
import com.inari.firefly.system.FFContext

abstract class SystemComponentBuilder<out C : SystemComponent> : ComponentBuilder<C>() {

    val build: (C.() -> Unit) -> CompId = builder()

    private fun doBuild(comp: C, configure: C.() -> Unit): CompId {
        comp.also(configure)
        receiver()(comp)
        return comp.componentId
    }
    private fun builder(): (C.() -> Unit) -> CompId = {
        configure -> doBuild(createEmpty(), configure)
    }
    private fun receiver(): (C) -> C =
            FFContext.componentMapper<C>(typeKey).receiver()
}