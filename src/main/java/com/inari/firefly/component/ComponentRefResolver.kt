package com.inari.firefly.component

import com.inari.commons.lang.indexed.Indexed
import com.inari.firefly.Expr
import com.inari.firefly.FFContext
import com.inari.firefly.Named

class ComponentRefResolver<T : Component>(
    private val type: ComponentType<T>,
    private val receiver: Expr<Int>
) {

    var id : CompId
        get() = throw IllegalAccessException()
        set(value) { receiver(value.index) }

    var index : Int
        get() = throw IllegalAccessException()
        set(value) { receiver(value) }

    var indexed : Indexed
        get() = throw IllegalAccessException()
        set(value) { receiver(value.index()) }

    var name : String
        get() = throw IllegalAccessException()
        set(value) { receiver(FFContext[type, value].index()) }

    var named : Named
        get() = throw IllegalAccessException()
        set(value) { receiver(FFContext[type, value.name].index()) }
}