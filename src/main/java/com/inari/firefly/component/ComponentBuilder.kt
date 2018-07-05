package com.inari.firefly.component

import com.inari.util.indexed.IIndexedTypeKey


abstract class ComponentBuilder<out C : Component> {
    abstract val indexedTypeKey: IIndexedTypeKey
    protected abstract fun createEmpty(): C
}