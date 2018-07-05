package com.inari.firefly.asset

import com.inari.firefly.Expr
import com.inari.firefly.Named
import com.inari.firefly.component.CompId
import com.inari.util.indexed.Indexed

class AssetInstanceRefResolver(
    private val receiver: Expr<Int>,
    private val i: Int = 0
) {

    operator fun invoke(id: CompId) =
        receiver( AssetSystem.assets[id.index].instanceId() )
    operator fun invoke(index: Int) =
        receiver(AssetSystem.assets[index].instanceId())
    operator fun invoke(indexed: Indexed) =
        receiver(AssetSystem.assets[indexed.index].instanceId())
    operator fun invoke(name: String) =
        receiver(AssetSystem.assets[name].instanceId())
    operator fun invoke(named: Named) =
        receiver(AssetSystem.assets[named.name].instanceId())

    var instanceId : Int
        get() = throw IllegalAccessException()
        set(value) { receiver(value) }
}