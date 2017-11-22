package com.inari.firefly.misc

import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.component.CompId

interface TestEventListener {
    fun notifyComponentCreation(id: CompId, key: IndexedTypeKey)
}