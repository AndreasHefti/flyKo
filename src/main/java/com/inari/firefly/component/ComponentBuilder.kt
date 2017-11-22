package com.inari.firefly.component

import com.inari.commons.lang.indexed.IndexedTypeKey

abstract class ComponentBuilder<out C : Component> {
    abstract val typeKey: IndexedTypeKey
    protected abstract fun createEmpty(): C
}