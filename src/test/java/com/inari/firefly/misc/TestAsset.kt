package com.inari.firefly.misc

import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.asset.Asset
import com.inari.firefly.component.ComponentSubType
import com.inari.firefly.system.component.SystemComponentBuilder

class TestAsset(
        var ff_Param1: String = "",
        var ff_Param2: Float = 0.0f
) : Asset() {

    private constructor() : this("", 0.0f)

    private var instanceId: Int = -1
    override fun instanceId(index: Int): Int = instanceId

    override fun load() {
        println("TestAsset: load")
    }

    override fun unload() {
        println("TestAsset: unload")
    }

    companion object : SystemComponentBuilder<TestAsset>(), ComponentSubType<TestAsset, Asset> {
        override val typeKey: IndexedTypeKey = Asset.typeKey
        override fun subType(): Class<TestAsset> = TestAsset::class.java
        override fun createEmpty(): TestAsset = TestAsset()
    }
}