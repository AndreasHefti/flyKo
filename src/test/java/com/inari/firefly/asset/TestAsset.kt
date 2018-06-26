package com.inari.firefly.asset

import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.system.component.SubType

class TestAsset private constructor(
        var ff_Param1: String = "",
        var ff_Param2: Float = 0.0f
) : Asset() {

    var ff_DependsOn =
        ComponentRefResolver(Asset, { index-> dependingRef = setIfNotInitialized(index, "ff_DependsOn") })

    private var instanceId: Int = -1
    override fun instanceId(index: Int): Int = instanceId

    override fun load() {
        instanceId = 1
    }

    override fun unload() {
        instanceId = -1
    }

    override fun toString(): String {
        return "TestAsset(name='${name}', " +
            "ff_Param1='$ff_Param1', " +
            "ff_Param2=$ff_Param2, " +
            "instanceId=$instanceId)" +
            " dependsOn=$dependingRef"
    }

    companion object : SubType<TestAsset, Asset>() {
        override val typeKey: IndexedTypeKey = Asset.typeKey
        override fun subType(): Class<TestAsset> = TestAsset::class.java
        override fun createEmpty(): TestAsset = TestAsset()
    }
}