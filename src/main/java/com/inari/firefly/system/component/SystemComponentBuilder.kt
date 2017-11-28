package com.inari.firefly.system.component

import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentBuilder
import com.inari.firefly.FFContext

abstract class SystemComponentBuilder<C : SystemComponent> : ComponentBuilder<C>() {

    val build: (C.() -> Unit) -> CompId = { configure ->
        val comp: C = createEmpty()
        comp.also(configure)
        receiver()(comp)
        comp.componentId
    }

    val buildAndGet: (C.() -> Unit) -> C = { configure ->
        val comp: C = createEmpty()
        comp.also(configure)
        receiver()(comp)
        comp
    }

    val buildAndActivate: (C.() -> Unit) -> CompId = { configure ->
        val comp: C = createEmpty()
        comp.also(configure)
        receiver()(comp)
        FFContext.activate(comp.componentId)
        comp.componentId
    }

    val buildActivateAndGet: (C.() -> Unit) -> C = { configure ->
        val comp: C = createEmpty()
        comp.also(configure)
        receiver()(comp)
        FFContext.activate(comp.componentId)
        comp
    }

    protected fun receiver(): (C) -> C =
        FFContext.mapper<C>(typeKey).receiver()
}