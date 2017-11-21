package com.inari.firefly.misc

import com.inari.commons.lang.aspect.IAspects
import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.component.ComponentMap
import com.inari.firefly.component.ComponentType
import com.inari.firefly.component.IComponentMap
import com.inari.firefly.system.FFContext
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent

object TestComponentSystem : ComponentSystem, TestEventListener {

    init {
        FFContext.registerListener(StaticTestEvent.EVENT_KEY, this)
    }

    override val supportedComponents: IAspects = SystemComponent.ASPECT_GROUP.createAspects(
            TestComponent.typeKey,
            Test2Component.typeKey
    )

    val comps1: IComponentMap<TestComponent> = ComponentMap(cBuilder = TestComponent)
    val comps2: IComponentMap<Test2Component> = ComponentMap(cBuilder = Test2Component)
    val buildComp1 = comps1.build
    val buildComp2 = comps2.build


    @Suppress("UNCHECKED_CAST")
    override fun <C : SystemComponent> with(type: ComponentType<C>): IComponentMap<C> = when (type) {
        TestComponent -> comps1 as IComponentMap<C>
        Test2Component -> comps2 as IComponentMap<C>
        else -> throw RuntimeException("error")
    }


    override fun notifyComponentCreation(id: Int, key: IndexedTypeKey) =
            println("event: $id $key")

    override fun clearSystem() {
        comps1.clear()
        comps2.clear()
    }
}