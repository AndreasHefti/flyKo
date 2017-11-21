package com.inari.firefly.system.component

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.component.ComponentType
import com.inari.firefly.component.IComponentMap

interface ComponentSystem {
    val supportedComponents: IAspects
    fun <C : SystemComponent> with(type: ComponentType<C>): IComponentMap<C>
    fun clearSystem()
}