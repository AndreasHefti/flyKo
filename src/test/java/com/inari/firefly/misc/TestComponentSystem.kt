package com.inari.firefly.misc

import com.inari.commons.lang.aspect.IAspects
import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.component.CompId
import com.inari.firefly.component.mapping.ComponentMap
import com.inari.firefly.system.FFContext
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent

object TestComponentSystem : ComponentSystem, TestEventListener {

    private val c1: ComponentMap<TestComponent> = ComponentMap(TestComponent)
    private val c2: ComponentMap<Test2Component> = ComponentMap(Test2Component)

    init {
        FFContext.registerComponentMapper(c1)
        FFContext.registerComponentMapper(c2)
        FFContext.registerListener(StaticTestEvent.EVENT_KEY, this)
    }

    override val supportedComponents: IAspects = SystemComponent.ASPECT_GROUP.createAspects(
        TestComponent.typeKey,
        Test2Component.typeKey
    )

    override fun notifyComponentCreation(id: CompId, key: IndexedTypeKey) =
            println("event: $id $key")

    override fun clearSystem() {
        c1.clear()
        c2.clear()
    }

    override fun dispose() {
        FFContext.disposeComponentMapper(c1)
        FFContext.disposeComponentMapper(c2)
    }
}