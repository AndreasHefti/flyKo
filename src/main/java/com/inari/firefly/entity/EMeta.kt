package com.inari.firefly.entity

import com.inari.commons.lang.list.IntBag
import com.inari.commons.lang.list.IntBagRO
import com.inari.firefly.NO_NAME
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.component.NamedComponent
import com.inari.firefly.control.Controller

class EMeta private constructor() : EntityComponent(), NamedComponent {

    @JvmField internal var name: String = NO_NAME
    @JvmField internal val controller: IntBag = IntBag(5, -1, 5)

    var ff_Name: String
        get() = name
        set(value) { name = setIfNotInitialized(value, "ff_Name") }

    val ff_Controller: IntBagRO
        get() = controller

    val ff_addController =
        ComponentRefResolver(Controller, { index-> if (index !in controller) controller.add(index) })
    val ff_removeController =
        ComponentRefResolver(Controller, { index-> controller.remove(index) })

    override fun indexedTypeKey() = typeKey
    override fun name(): String = name
    override fun reset() {
        name = NO_NAME
        controller.clear()
    }

    override fun toString(): String =
        "EMeta(name='$name', controller=$controller)"

    companion object : EntityComponentType<EMeta>() {
        override val typeKey = EntityComponent.createTypeKey(EMeta::class.java)
        override fun createEmpty() = EMeta()
    }
}