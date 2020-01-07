package com.inari.firefly.component

import com.inari.firefly.Consumer
import com.inari.firefly.FFContext
import com.inari.firefly.Named
import com.inari.firefly.system.component.SingletonComponent
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.indexed.Indexed

class ComponentRefResolver<T : Component>(
    private val type: ComponentType<T>,
    private val receiver: Consumer<Int>
) {

    operator fun invoke(id: CompId) = receiver(id.instanceId)
    operator fun invoke(index: Int) = receiver(index)
    operator fun invoke(indexed: Indexed) = receiver(indexed.index)
    operator fun invoke(name: String) = receiver(FFContext[type, name].index)
    operator fun invoke(named: Named) = receiver(FFContext[type, named.name].index)
    operator fun invoke(component: Component) = receiver(component.index)
    operator fun invoke(component: SystemComponent) = receiver(component.index)
    operator fun invoke(singleton: SingletonComponent<*, *>) = receiver(singleton.instance.index)

}