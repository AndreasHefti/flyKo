package com.inari.firefly.entity

import com.inari.firefly.NO_NAME
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.component.NamedComponent
import com.inari.firefly.control.Controller
import com.inari.firefly.control.SingleComponentController
import com.inari.util.aspect.Aspects
import com.inari.util.aspect.IndexedAspectType

class EMeta private constructor() : EntityComponent(EMeta::class.java.name), NamedComponent {

    @JvmField internal var controllerRef = -1
    @Suppress("SetterBackingFieldAssignment")
    var aspects: Aspects = ENTITY_META_ASPECTS.createAspects()
        set(value) {
            field.clear()
            field + value
        }
    override var name: String  = NO_NAME
        set(value) {
            check(!(name !== NO_NAME)) { "An illegal reassignment of name: $value to: $name" }
            field = name
        }

    val controller = ComponentRefResolver(Controller) { index-> controllerRef = index }
    fun controller(configure: (SingleComponentController.() -> Unit)): CompId {
        val id = SingleComponentController.build(configure)
        controllerRef = id.index
        return id
    }
    fun activeController(configure: (SingleComponentController.() -> Unit)): CompId {
        val id = SingleComponentController.buildAndActivate(configure)
        controllerRef = id.index
        return id
    }

    override fun reset() {
        name = NO_NAME
        aspects.clear()
        controllerRef = -1
    }

    override fun toString(): String =
        "EMeta(controllerRef=$controllerRef, name='$name')"

    override fun componentType() = Companion
    companion object : EntityComponentType<EMeta>(EMeta::class.java) {
        override fun createEmpty() = EMeta()
        val ENTITY_META_ASPECTS = IndexedAspectType("ENTITY_META_ASPECTS")
    }
}