package com.inari.firefly.system.component

import com.inari.firefly.component.NamedComponent
import com.inari.commons.lang.aspect.AspectGroup
import com.inari.commons.lang.indexed.*
import com.inari.firefly.component.CompId
import com.inari.firefly.system.Constants.NO_NAME

private const val NAME_NOT_ASSIGNED: String = "[[NAME_NOT_ASSIGNED]]"
abstract class SystemComponent : BaseIndexedObject(), IndexedType, NamedComponent {


    var name: String = NAME_NOT_ASSIGNED
    var ff_Name: String
        set(ff_Name) {
            if (name != NAME_NOT_ASSIGNED) {
                throw IllegalStateException("Illegal reassignment of name")
            }
            name = ff_Name
        }
        get() = name


    final override val componentId: CompId = CompId(index, indexedTypeKey())
    final override fun indexedObjectType(): Class<out IndexedObject> = indexedTypeKey().type<IndexedObject>()
    override fun name(): String = ff_Name

    companion object {

        val ASPECT_GROUP = AspectGroup("SystemComponent")

        fun <T: SystemComponent> createTypeKey(type: Class<T>): IndexedTypeKey = Indexer.createIndexedTypeKey(TypeKey::class.java, type)

        class TypeKey<out C : SystemComponent> private constructor(indexedType: Class<C>) : IndexedTypeKey(indexedType) {
            override fun aspectGroup(): AspectGroup = ASPECT_GROUP
            override fun baseType(): Class<SystemComponent> = SystemComponent::class.java
            @Suppress("UNCHECKED_CAST") fun baseComponentType(): Class<out C> = indexedType as Class<out C>
            override fun toString(): String = "SystemComponent:" + type<C>().simpleName;
        }
    }
}