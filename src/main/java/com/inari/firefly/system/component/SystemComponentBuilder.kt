package com.inari.firefly.system.component

import com.inari.firefly.FFContext
import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentBuilder
import com.inari.firefly.component.Singleton

abstract class SystemComponentBuilder<out C : SystemComponent> : ComponentBuilder<C>() {

    var singletonId: CompId = NO_COMP_ID
        private set

    val build: (C.() -> Unit) -> CompId = { configure ->
        val comp: C = createEmpty()
        checkSingleton(comp)
        comp.also(configure)
        comp._init()
        FFContext.mapper<C>(typeKey).receiver()(comp)
        comp.componentId
    }

    val buildAndGet: (C.() -> Unit) -> C = { configure ->
        val comp: C = createEmpty()
        checkSingleton(comp)
        comp.also(configure)
        comp._init()
        FFContext.mapper<C>(typeKey).receiver()(comp)
        comp
    }

    val buildAndActivate: (C.() -> Unit) -> CompId = { configure ->
        val comp: C = createEmpty()
        checkSingleton(comp)
        comp.also(configure)
        comp._init()
        FFContext.mapper<C>(typeKey).receiver()(comp)
        FFContext.activate(comp.componentId)
        comp.componentId
    }

    val buildActivateAndGet: (C.() -> Unit) -> C = { configure ->
        val comp: C = createEmpty()
        checkSingleton(comp)
        comp.also(configure)
        comp._init()
        FFContext.mapper<C>(typeKey).receiver()(comp)
        FFContext.activate(comp.componentId)
        comp
    }

    private fun checkSingleton(comp: C) {
        if (comp::class.java.isAnnotationPresent(Singleton::class.java)) {
            if (singletonId !== NO_COMP_ID) {
                throw UnsupportedOperationException("")
            }

            singletonId = comp.componentId
        }
    }
}