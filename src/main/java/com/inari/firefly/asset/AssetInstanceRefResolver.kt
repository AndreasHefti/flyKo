package com.inari.firefly.asset

import com.inari.commons.lang.indexed.Indexed
import com.inari.firefly.Expr
import com.inari.firefly.Named
import com.inari.firefly.component.CompId

class AssetInstanceRefResolver(
    private val receiver: Expr<Int>,
    private val i: Int = 0
) {

    var id : CompId
        get() = throw IllegalAccessException()
        set(value) { comp = AssetSystem.assets[value.index] }

    var index : Int
        get() = throw IllegalAccessException()
        set(value) { comp = AssetSystem.assets[value] }

    var indexed : Indexed
        get() = throw IllegalAccessException()
        set(value) { comp = AssetSystem.assets[value.index()] }

    var name : String
        get() = throw IllegalAccessException()
        set(value) { comp = AssetSystem.assets[value] }

    var named : Named
        get() = throw IllegalAccessException()
        set(value) { comp = AssetSystem.assets[value.name] }

    var instanceId : Int
        get() = throw IllegalAccessException()
        set(value) { receiver(value) }

    var comp : Asset
        get() = throw IllegalAccessException()
        set(value) { receiver(value.instanceId(i)) }
}