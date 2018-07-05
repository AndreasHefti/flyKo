package com.inari.firefly.entity

import com.inari.firefly.component.CompId
import com.inari.firefly.component.Component
import com.inari.firefly.component.ComponentType
import com.inari.firefly.entity.Entity.EntityComponentBuilder
import com.inari.util.aspect.AspectGroup
import com.inari.util.indexed.IIndexedTypeKey
import com.inari.util.indexed.IndexedType
import com.inari.util.indexed.IndexedTypeKey
import com.inari.util.indexed.Indexer

abstract class EntityComponent protected constructor() : Component, IndexedType {
    
    /** An EntityComponent instance has no object index (-1) only a subType index
     *  supported by the index subType key
     */
    final override val componentId: CompId
     by lazy { CompId(-1, indexedTypeKey) }
    /** For an EntityComponent always the subType index is given as the index */
    final override val index: Int get() = componentId.typeKey.index

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

    override val indexedTypeKey: IIndexedTypeKey
        get() = componentType().indexedTypeKey

    abstract fun componentType(): ComponentType<out EntityComponent>

    companion object {

        val ENTITY_COMPONENT_ASPECTS = AspectGroup("EntityComponentTypeKey")

        fun <T: EntityComponent> create(subType: Class<T>): IIndexedTypeKey =
            Indexer.getOrCreateIndexedTypeKey(
                EntityTypeKey::class.java,
                subType
            ) { EntityTypeKey(subType) }

        internal class EntityTypeKey(subType: Class<out EntityComponent>) :
            IndexedTypeKey(EntityComponent::class.java, subType, ENTITY_COMPONENT_ASPECTS)
    }

    abstract class EntityComponentType<C : EntityComponent> : EntityComponentBuilder<C>()

}