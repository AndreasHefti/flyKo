package com.inari.firefly.component

import com.inari.firefly.Expr
import com.inari.firefly.FFContext

class ComponentRefResolver<T : Component>(
    private val type: ComponentType<T>,
    private val receiver: Expr<Int>
) {

    var id : CompId
        get() = throw IllegalAccessException()
        set(value) {receiver(value.index)}

    var index : Int
        get() = throw IllegalAccessException()
        set(value) {receiver(value)}

    var name : String
        get() = throw IllegalAccessException()
        set(value) {receiver(FFContext[type, value].index())}

    var comp : T
        get() = throw IllegalAccessException()
        set(value) {receiver(value.index())}
}