package com.inari.firefly.component

import com.inari.commons.lang.indexed.IIndexedTypeKey

abstract class ComponentBuilder<out C : Component> {
    abstract val typeKey: IIndexedTypeKey
    protected abstract fun createEmpty(): C
}