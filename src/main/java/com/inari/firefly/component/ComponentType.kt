package com.inari.firefly.component

import com.inari.commons.lang.indexed.IndexedTypeKey

interface ComponentType<C : Component> {
    val typeKey: IndexedTypeKey
    fun type(): Class<C> = typeKey.type<C>()
}