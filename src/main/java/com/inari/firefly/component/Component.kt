package com.inari.firefly.component

import com.inari.util.indexed.Indexed


@ComponentDSL
interface Component : Indexed {
    val componentId: CompId
    fun dispose()
}