package com.inari.firefly.physics.contact

import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.graphics.view.Layer
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentSingleType
import com.inari.util.aspect.Aspects
import com.inari.util.geom.Rectangle

class ContactConstraint private constructor() : SystemComponent(ContactConstraint::class.java.name) {

    @JvmField internal var layerRef = -1
    @JvmField internal val bounds = Rectangle()
    @JvmField internal val typeFilter = ContactSystem.CONTACT_TYPE_ASPECT_GROUP.createAspects()
    @JvmField internal val materialFilter = ContactSystem.MATERIAL_ASPECT_GROUP.createAspects()

    val ff_Layer =
        ComponentRefResolver(Layer) { index->
            layerRef = setIfNotInitialized(index, "ff_Layer")
        }

    var ff_Bounds: Rectangle
        get() = bounds
        set(value) { bounds(value) }

    val ff_MaterialFilter: Aspects
        get() = materialFilter

    val ff_TypeFilter: Aspects
        get() = typeFilter

    val width: Int
        get() = bounds.width

    val height: Int
        get() = bounds.height

    val pivotX: Int
        get() = bounds.x

    val pivotY: Int
        get() = bounds.y

    val isFiltering: Boolean
        get() = !materialFilter.isEmpty

    fun match(contact: EContact): Boolean =
            (typeFilter.isEmpty || contact.contactType in typeFilter) &&
                    (materialFilter.isEmpty || contact.material in materialFilter)



    override fun componentType() = Companion
    companion object : SystemComponentSingleType<ContactConstraint>(ContactConstraint::class.java) {
        override fun createEmpty() = ContactConstraint()
    }
}