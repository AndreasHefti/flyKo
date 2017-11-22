package com.inari.firefly.component

import com.inari.commons.lang.indexed.Indexed

@ComponentDSL
interface Component : Indexed {
    val componentId: CompId
    fun dispose()
}