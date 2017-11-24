package com.inari.firefly.component

import com.inari.commons.lang.indexed.IIndexedTypeKey

interface ComponentType<C : Component> {
    val typeKey: IIndexedTypeKey
    fun type(): Class<C> = typeKey.type()
}