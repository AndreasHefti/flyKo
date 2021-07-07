package com.inari.firefly.control

import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.component.Component
import com.inari.firefly.component.ComponentIdResolver
import com.inari.firefly.system.component.SingletonComponent
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentBuilder
import com.inari.util.Named

interface ControlledSystemComponent {

    val index: Int

    fun withController(id: CompId) =  FFContext[Controller, id].controlled.set(index)
    fun withController(name: String) = FFContext[Controller, name].controlled.set(index)
    fun withController(named: Named) = FFContext[Controller, named].controlled.set(index)
    fun withController(component: Controller) = component.controlled.set(index)

    fun <C : Controller> withController(cBuilder: SystemComponentBuilder<C>, configure: (C.() -> Unit)): CompId {
        val comp = cBuilder.buildAndGet(configure)
        comp.controlled.set(this.index)
        return comp.componentId
    }

    fun <C : Controller> withActiveController(cBuilder: SystemComponentBuilder<C>, configure: (C.() -> Unit)): CompId {
        val comp = cBuilder.buildActivateAndGet(configure)
        comp.controlled.set(this.index)
        return comp.componentId
    }
}
