package com.inari.firefly.asset

import com.inari.firefly.Expr
import com.inari.firefly.component.CompId

class AssetInstanceRefResolver(
    private val receiver: Expr<Int>,
    private val i: Int = 0
) {

    var id : CompId
        get() = throw IllegalAccessException()
        set(value) {comp = AssetSystem.assets[id.index]}

    var index : Int
        get() = throw IllegalAccessException()
        set(value) {comp = AssetSystem.assets[index]}

    var instanceInd : Int
        get() = throw IllegalAccessException()
        set(value) {receiver(instanceInd)}

    var name : String
        get() = throw IllegalAccessException()
        set(value) {comp = AssetSystem.assets[name]}

    var comp : Asset
        get() = throw IllegalAccessException()
        set(value) {receiver(value.instanceId(i))}
}