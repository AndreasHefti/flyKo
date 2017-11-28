package com.inari.firefly.system.component

import com.inari.commons.lang.aspect.AspectGroup
import com.inari.commons.lang.indexed.*
import com.inari.firefly.component.CompId
import com.inari.firefly.component.NamedComponent
import com.inari.firefly.NO_NAME

abstract class SystemComponent protected constructor() : BaseIndexedObject(), IndexedType, NamedComponent {

    private var nameInUse = false
    var ff_Name: String = NO_NAME
        set(ff_Name) {
            if (nameInUse) {
                throw IllegalStateException("Illegal reassignment of name: $ff_Name to: $ff_Name" )
            }
            field = ff_Name
        }

    final override val componentId: CompId = CompId(index, indexedTypeKey())
    final override fun indexedObjectType(): Class<out IndexedObject> = indexedTypeKey().type<IndexedObject>()
    override fun name(): String {
        if (ff_Name != NO_NAME) {
            nameInUse = true
        }
        return ff_Name
    }

    companion object {

        val ASPECT_GROUP = AspectGroup("SystemComponent")

        fun <T: SystemComponent> createTypeKey(type: Class<T>): IndexedTypeKey = Indexer.createIndexedTypeKey(TypeKey::class.java, type)

        class TypeKey<out C : SystemComponent> private constructor(indexedType: Class<C>) : IndexedTypeKey(indexedType) {
            override fun aspectGroup(): AspectGroup = ASPECT_GROUP
            override fun baseType(): Class<SystemComponent> = SystemComponent::class.java
            @Suppress("UNCHECKED_CAST") fun baseComponentType(): Class<out C> = indexedType as Class<out C>
            override fun toString(): String = "SystemComponent:" + type<C>().simpleName
        }
    }
}