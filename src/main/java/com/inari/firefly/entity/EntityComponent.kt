package com.inari.firefly.entity

import com.inari.commons.lang.aspect.AspectGroup
import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.commons.lang.indexed.IndexedType
import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.commons.lang.indexed.Indexer
import com.inari.firefly.component.CompId
import com.inari.firefly.component.Component
import com.inari.firefly.entity.Entity.EntityComponentBuilder

abstract class EntityComponent protected constructor() : Component, IndexedType {
    
    /** An EntityComponent instance has no object index (-1) only a type index
     *  supported by the index type key
     */
    final override val componentId: CompId = CompId(-1, getTypeKey())
    /** For an EntityComponent always the type index is given as the index */
    final override fun index(): Int = componentId.typeKey.index()

    final override fun dispose() = EntityProvider.dispose(this)

    var initialized: Boolean = false
        protected set
    internal fun _init() {
        init()
        initialized = true
    }
    protected fun init() {
        initialized = true
    }
    internal fun _reset() {
        reset()
        initialized = false
    }

    protected fun setIfNotInitialized(value: Int, name: String): Int =
        if (initialized) alreadyInit(name)
        else value

    protected fun setIfNotInitialized(value: Float, name: String): Float =
        if (initialized) alreadyInit(name)
        else value

    protected fun setIfNotInitialized(value: Boolean, name: String): Boolean =
        if (initialized) alreadyInit(name)
        else value

    protected fun <T> setIfNotInitialized(value:T, name: String): T =
        if (initialized) alreadyInit(name)
        else value

    private fun <T> alreadyInit(name: String): T =
        throw IllegalStateException("No set on already initialized property allowed for: $name")

    protected abstract fun reset()

    private fun getTypeKey(): IIndexedTypeKey = indexedTypeKey()

    companion object {

        val ENTITY_ASPECTS = AspectGroup("EntityComponentTypeKey")

        fun <T: EntityComponent> createTypeKey(type: Class<T>): IndexedTypeKey =
                Indexer.createIndexedTypeKey(TypeKey::class.java, type)

        class TypeKey<out C : EntityComponent> private constructor(indexedType: Class<C>) : IndexedTypeKey(indexedType) {
            override fun aspectGroup(): AspectGroup = ENTITY_ASPECTS
            override fun baseType(): Class<EntityComponent> = EntityComponent::class.java
            @Suppress("UNCHECKED_CAST") fun baseComponentType(): Class<out C> = indexedType as Class<out C>
            override fun toString(): String = "EntityComponent:" + type<C>().simpleName
        }
    }

    abstract class EntityComponentType<C : EntityComponent> : EntityComponentBuilder<C>()

}