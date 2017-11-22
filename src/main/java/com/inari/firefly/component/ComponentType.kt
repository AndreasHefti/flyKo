package com.inari.firefly.component

import com.inari.commons.lang.indexed.IndexedTypeKey

interface ComponentType<C : Component> {
    val typeKey: IndexedTypeKey
    val subType: Class<C>
}