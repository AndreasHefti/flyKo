package com.inari.firefly.entity

import com.inari.commons.lang.aspect.AspectGroup
import com.inari.commons.lang.aspect.Aspects
import com.inari.firefly.NO_NAME
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.component.NamedComponent
import com.inari.firefly.control.Controller

class EMeta private constructor() : EntityComponent(), NamedComponent {

    @JvmField internal var controllerRef = -1
    @JvmField internal var name: String = NO_NAME
    @JvmField internal val aspects = ENTITY_META_ASPECTS.createAspects()

    var ff_Name: String
        get() = name
        set(value) { name = setIfNotInitialized(value, "ff_Name") }
    val ff_Controller =
        ComponentRefResolver(Controller, { index->
            controllerRef = setIfNotInitialized(index, "ff_Controller")
        })
    var ff_Aspects: Aspects
        get() = aspects
        set(value) {
            aspects.clear()
            aspects.set(value)
        }


    override fun indexedTypeKey() = typeKey
    override fun name(): String = name
    override fun reset() {
        name = NO_NAME
        controllerRef = -1
    }

    override fun toString(): String {
        return "EMeta(controllerRef=$controllerRef, name='$name')"
    }

    companion object : EntityComponentType<EMeta>() {
        override val typeKey = EntityComponent.createTypeKey(EMeta::class.java)
        override fun createEmpty() = EMeta()

        val ENTITY_META_ASPECTS = AspectGroup("ENTITY_META_ASPECTS")
    }
}