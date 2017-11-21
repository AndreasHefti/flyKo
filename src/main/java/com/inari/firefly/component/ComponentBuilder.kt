package com.inari.firefly.component

interface ComponentBuilder<C : Component> : ComponentType<C> {
    fun builder(registry: (C) -> C): (C.() -> Unit) -> Int
    fun build(comp: C, configure: C.() -> Unit, registry: (C) -> C): Int {
        comp.also(configure)
        registry(comp)
        return comp.index()
    }
}