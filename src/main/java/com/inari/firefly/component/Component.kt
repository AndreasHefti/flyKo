package com.inari.firefly.component

import com.inari.firefly.Named
import com.inari.util.aspect.Aspect
import com.inari.util.indexed.Indexed

@DslMarker
annotation class ComponentDSL

@ComponentDSL
interface Component : Indexed {
    val componentId: CompId
    fun dispose()
}

interface NamedComponent : Component, Named

interface ComponentType<C : Component> : Aspect {
    val typeClass: Class<out Component>
}

class CompId (
    val instanceId: Int,
    val componentType: ComponentType<*>
) {
    fun checkType(typeAspect: Aspect): CompId {
        componentType.aspectType.typeCheck(typeAspect)
        return this
    }
}

interface CompNameId: Named {
    val componentType: ComponentType<*>
}

abstract class ComponentBuilder<out C : Component> {
    protected abstract fun createEmpty(): C
}