package com.inari.firefly.entity

import com.inari.firefly.NO_NAME
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.component.ComponentType
import com.inari.firefly.component.NamedComponent
import com.inari.firefly.control.Controller
import com.inari.util.aspect.Aspects
import com.inari.util.aspect.IndexedAspectType

class EMeta private constructor() : EntityComponent(), NamedComponent {

    @JvmField internal var controllerRef = -1
    override var name: String = NO_NAME
        private set
    @JvmField internal val aspects = ENTITY_META_ASPECTS.createAspects()


    var ff_Name: String
        set(ff_Name) {
            if (name !== com.inari.firefly.NO_NAME) {
                throw IllegalStateException("Illegal reassignment of name: $ff_Name to: $ff_Name" )
            }
            name = ff_Name
        }
        get() = name
    val ff_Controller = ComponentRefResolver(Controller) { index-> controllerRef = index }
    var ff_Aspects: Aspects
        get() = aspects
        set(value) {
            aspects.clear()
            aspects + value
        }

    override fun reset() {
        name = NO_NAME
        controllerRef = -1
    }

    override fun toString(): String =
        "EMeta(controllerRef=$controllerRef, name='$name')"

    override fun componentType(): ComponentType<EMeta> =
        EMeta.Companion

    companion object : EntityComponentType<EMeta>(EMeta::class.java) {
        override fun createEmpty() = EMeta()
        val ENTITY_META_ASPECTS = IndexedAspectType("ENTITY_META_ASPECTS")
    }
}