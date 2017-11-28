package com.inari.firefly.asset

import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.system.component.SubType

class TestAsset private constructor(
        var ff_Param1: String = "",
        var ff_Param2: Float = 0.0f
) : Asset() {

    private constructor() : this("", 0.0f)

    var ff_DependsOn: String
        set(value) {depending.name = value}
        get() = depending.name

    private var instanceId: Int = -1
    override fun instanceId(index: Int): Int = instanceId

    override fun load() {
        instanceId = 1
    }

    override fun unload() {
        instanceId = -1
    }

    override fun toString(): String {
        return "TestAsset(name='${name()}', " +
            "ff_Param1='$ff_Param1', " +
            "ff_Param2=$ff_Param2, " +
            "instanceId=$instanceId)" +
            if (depending.defined()) " dependsOn=$depending" else ""
    }

    companion object : SubType<TestAsset, Asset>() {
        override val typeKey: IndexedTypeKey = Asset.typeKey
        override fun subType(): Class<TestAsset> = TestAsset::class.java
        override fun createEmpty(): TestAsset = TestAsset()
    }
}