package com.inari.firefly.component

import com.inari.commons.lang.indexed.Indexed

interface NamedComponent : Component {
    fun name(): String
}