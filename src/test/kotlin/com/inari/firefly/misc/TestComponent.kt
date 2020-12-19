package com.inari.firefly.misc

import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentSingleType

class TestComponent private constructor (
    var Param1: String,
    override var Param2: Int
) : SystemComponent(TestComponent::class.java.name), ITestComponent {

    private constructor() : this("Param1", 0)

    override fun componentType() =
        TestComponent.Companion

    companion object : SystemComponentSingleType<TestComponent>(TestComponent::class.java) {
        override fun createEmpty(): TestComponent = TestComponent()
    }

}