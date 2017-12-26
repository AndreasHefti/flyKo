package com.inari.firefly.physics.contact

import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.aspect.Aspect
import com.inari.commons.lang.aspect.Aspects
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.graphics.view.Layer
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent

class ContactConstraint private constructor() : SystemComponent() {

    @JvmField internal var layerRef = -1
    @JvmField internal val bounds = Rectangle()
    @JvmField internal val materialFilter = ContactSystem.MATERIAL_ASPECT_GROUP.createAspects()

    val ff_Layer =
        ComponentRefResolver(Layer, { index->
            layerRef = setIfNotInitialized(index, "ff_Layer")
        })
    var ff_Bounds: Rectangle
        get() = bounds
        set(value) {bounds.setFrom(value)}
    val ff_MaterialFilter: Aspects
        get() = materialFilter

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

    fun match(materialType: Aspect): Boolean =
        if (materialFilter.isEmpty) true
        else materialType in materialFilter

    override fun indexedTypeKey() = typeKey
    companion object : SingleType<ContactConstraint>() {
        override val typeKey = SystemComponent.createTypeKey(ContactConstraint::class.java)
        override fun createEmpty() = ContactConstraint()
    }
}