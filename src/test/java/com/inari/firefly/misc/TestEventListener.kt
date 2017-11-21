package com.inari.firefly.misc

import com.inari.commons.lang.indexed.IndexedTypeKey

interface TestEventListener {
    fun notifyComponentCreation(id: Int, key: IndexedTypeKey)
}