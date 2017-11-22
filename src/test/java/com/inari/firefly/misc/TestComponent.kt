package com.inari.firefly.misc

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentBuilder

data class TestComponent private constructor(
        var ff_Name: String,
        var ff_Param1: String,
        var ff_Param2: Int
) : SystemComponent() {

    private constructor() : this("NONAME", "Param1", 0)

    override fun name(): String = ff_Name
    override fun indexedTypeKey(): IIndexedTypeKey = typeKey

    companion object : ComponentType<TestComponent> {
        override val typeKey = SystemComponent.createTypeKey(TestComponent::class.java)
        override val subType: Class<TestComponent> = typeKey.type()
    }

    object Builder : SystemComponentBuilder<TestComponent>() {
        override val typeKey: IndexedTypeKey = TestComponent.typeKey
        override fun createEmpty(): TestComponent = TestComponent()
    }


}