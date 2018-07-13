package com.inari.firefly.physics.contact

import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.graphics.view.Layer
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentSingleType
import com.inari.util.aspect.Aspect
import com.inari.util.aspect.Aspects
import com.inari.util.geom.Rectangle
import com.inari.util.indexed.Indexer

class ContactConstraint private constructor() : SystemComponent() {

    override val indexer: Indexer =
        Indexer(ContactConstraint::class.java.name)

    @JvmField internal var layerRef = -1
    @JvmField internal val bounds = Rectangle()
    @JvmField internal val materialFilter = ContactSystem.MATERIAL_ASPECT_GROUP.createAspects()

    val ff_Layer =
        ComponentRefResolver(Layer) { index->
            layerRef = setIfNotInitialized(index, "ff_Layer")
        }
    var ff_Bounds: Rectangle
        get() = bounds
        set(value) {bounds(value)}
    val ff_MaterialFilter: Aspects
        get() = materialFilter

    val width: Int
        get() = bounds.width
    val height: Int
        get() = bounds.height
    val pivotX: Int
        get() = bounds.pos.x
    val pivotY: Int
        get() = bounds.pos.y
    val isFiltering: Boolean
        get() = !materialFilter.isEmpty

    fun match(materialType: Aspect): Boolean =
        if (materialFilter.isEmpty) true
        else materialType in materialFilter

    override fun componentType() =
        ContactConstraint.Companion

    companion object : SystemComponentSingleType<ContactConstraint>(ContactConstraint::class.java) {
        override fun createEmpty() = ContactConstraint()
    }
}