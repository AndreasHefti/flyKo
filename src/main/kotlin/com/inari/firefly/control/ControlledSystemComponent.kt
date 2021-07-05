package com.inari.firefly.control

import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.component.CompId
import com.inari.firefly.control.SystemComponentController
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentBuilder

abstract class ControlledSystemComponent protected constructor(
    objectIndexerName: String
) : SystemComponent(objectIndexerName) {

    var controllerId: CompId = NO_COMP_ID
        private set

    fun <C : SystemComponentController> withController(cBuilder: SystemComponentBuilder<C>, configure: (C.() -> Unit)): CompId {
        val comp = cBuilder.buildAndGet(configure)
        comp.controlledComponentId = this.componentId
        this.controllerId = comp.componentId
        return comp.componentId
    }
}
