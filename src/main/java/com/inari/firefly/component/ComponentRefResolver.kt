package com.inari.firefly.component

import com.inari.commons.lang.indexed.Indexed
import com.inari.firefly.Expr
import com.inari.firefly.FFContext
import com.inari.firefly.Named

class ComponentRefResolver<T : Component>(
    private val type: ComponentType<T>,
    private val receiver: Expr<Int>
) {

    operator fun invoke(id: CompId) = receiver(id.index)
    operator fun invoke(index: Int) = receiver(index)
    operator fun invoke(indexed: Indexed) = receiver(indexed.index())
    operator fun invoke(name: String) = receiver(FFContext[type, name].index())
    operator fun invoke(named: Named) = receiver(FFContext[type, named.name].index())

}