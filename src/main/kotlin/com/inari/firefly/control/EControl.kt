package com.inari.firefly.control

import com.inari.firefly.component.CompId
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.firefly.system.component.SystemComponentBuilder
import com.inari.util.aspect.Aspects
import com.inari.util.aspect.IndexedAspectType

class EControl private constructor() : EntityComponent(EControl::class.simpleName!!) {

    @Suppress("SetterBackingFieldAssignment")
    var aspects: Aspects = ENTITY_CONTROL_ASPECTS.createAspects()
        set(value) {
            field.clear()
            field + value
        }

    fun <C : Controller> withController(cBuilder: SystemComponentBuilder<C>, configure: (C.() -> Unit)): CompId {
        val comp = cBuilder.buildAndGet(configure)
        comp.controlled.set(this.index)
        return comp.componentId
    }

    override fun reset() {
        aspects.clear()
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<EControl>(EControl::class) {
        override fun createEmpty() = EControl()
        val ENTITY_CONTROL_ASPECTS = IndexedAspectType("ENTITY_CONTROL_ASPECTS")
    }
}