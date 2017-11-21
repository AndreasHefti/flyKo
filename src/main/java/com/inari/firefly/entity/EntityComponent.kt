package com.inari.firefly.entity

import com.inari.commons.lang.aspect.AspectGroup
import com.inari.commons.lang.indexed.IndexedType
import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.commons.lang.indexed.Indexer
import com.inari.firefly.component.CompId
import com.inari.firefly.component.Component

abstract class EntityComponent : Component, IndexedType {
    protected constructor()

    /** An EntityComponent instance has no object index (-1) only a type index
     *  supported by the index type key
     */
    final override val componentId: CompId = CompId(-1, indexedTypeKey())
    /** For an EntityComponent always the type index is given as the index */
    final override fun index(): Int = componentId.typeKey().index()

    abstract fun reset()

    companion object {

        val ASPECT_GROUP = AspectGroup("EntityComponentTypeKey")

        fun <T: EntityComponent> createTypeKey(type: Class<T>): IndexedTypeKey = Indexer.createIndexedTypeKey(TypeKey::class.java, type)

        class TypeKey<out C : EntityComponent> private constructor(indexedType: Class<C>) : IndexedTypeKey(indexedType) {
            override fun aspectGroup(): AspectGroup = ASPECT_GROUP
            override fun baseType(): Class<EntityComponent> = EntityComponent::class.java
            @Suppress("UNCHECKED_CAST") fun baseComponentType(): Class<out C> = indexedType as Class<out C>
            override fun toString(): String = "EntityComponent:" + type<C>().simpleName;
        }
    }
}