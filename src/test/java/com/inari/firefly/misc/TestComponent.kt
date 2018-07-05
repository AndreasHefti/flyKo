package com.inari.firefly.misc

import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent

class TestComponent private constructor (
    var ff_Param1: String,
    override var ff_Param2: Int
) : SystemComponent(), ITestComponent {


    private constructor() : this("Param1", 0)

    override fun componentType(): ComponentType<TestComponent> =
        TestComponent.Companion

    companion object : SingleType<TestComponent>() {
        override val indexedTypeKey by lazy { TypeKeyBuilder.create(TestComponent::class.java) }
        override fun createEmpty(): TestComponent = TestComponent()
    }

}