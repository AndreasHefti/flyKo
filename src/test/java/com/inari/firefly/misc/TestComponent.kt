package com.inari.firefly.misc

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent

class TestComponent private constructor (
    var ff_Param1: String,
    override var ff_Param2: Int
) : SystemComponent(), ITestComponent {


    private constructor() : this("Param1", 0)

    override fun indexedTypeKey(): IIndexedTypeKey = typeKey

    companion object : SingleType<TestComponent>() {
        override val typeKey: IIndexedTypeKey =
                SystemComponent.createTypeKey(TestComponent::class.java)
        override fun createEmpty(): TestComponent = TestComponent()
    }

}