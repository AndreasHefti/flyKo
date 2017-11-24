package com.inari.firefly.misc

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.firefly.component.ComponentSingleType
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentBuilder

class TestComponent private constructor(
        var ff_Param1: String,
        var ff_Param2: Int
) : SystemComponent() {


    private constructor() : this("Param1", 0)

    override fun indexedTypeKey(): IIndexedTypeKey = typeKey

    companion object : ComponentSingleType<TestComponent> {
        override val typeKey: IIndexedTypeKey =
                SystemComponent.createTypeKey(TestComponent::class.java)
    }

    object Builder : SystemComponentBuilder<TestComponent>() {
        override val typeKey: IIndexedTypeKey = TestComponent.typeKey
        override fun createEmpty(): TestComponent = TestComponent()
    }


}