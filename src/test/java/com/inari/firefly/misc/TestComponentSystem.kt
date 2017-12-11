package com.inari.firefly.misc

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentMap
import com.inari.firefly.FFContext
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent

object TestComponentSystem : ComponentSystem {

    override val supportedComponents: IAspects
    val c1: ComponentMap<TestComponent>
    val c2: ComponentMap<Test2Component>

    init {
        supportedComponents = SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(
            TestComponent.typeKey,
            Test2Component.typeKey
        )

        c1 = ComponentSystem.createComponentMapping(TestComponent)
        c2 = ComponentSystem.createComponentMapping(Test2Component)

        FFContext.registerListener(
            StaticTestEvent,
            {id: CompId ->  println("event: $id") }
        )
    }

    override fun clearSystem() {
        c1.clear()
        c2.clear()
    }

}