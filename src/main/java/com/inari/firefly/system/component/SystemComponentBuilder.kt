package com.inari.firefly.system.component

import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentBuilder
import com.inari.firefly.system.FFContext

abstract class SystemComponentBuilder<out C : SystemComponent> : ComponentBuilder<C>() {

    val build: (C.() -> Unit) -> CompId = { configure ->
        val comp: C = createEmpty()
        comp.also(configure)
        receiver()(comp)
        comp.componentId
    }

    private fun receiver(): (C) -> C =
            FFContext.mapper<C>(typeKey).receiver()
}