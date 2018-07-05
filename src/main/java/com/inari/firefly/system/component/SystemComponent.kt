package com.inari.firefly.system.component

import com.inari.firefly.component.CompId
import com.inari.firefly.component.NamedComponent
import com.inari.firefly.NO_NAME
import com.inari.firefly.component.ComponentType
import com.inari.util.aspect.AspectGroup
import com.inari.util.indexed.*

abstract class SystemComponent protected constructor() : BaseIndexedObject(), IndexedType, NamedComponent {

    override var name: String = NO_NAME
        protected set
    var ff_Name: String
        set(value) {
            if (name !== NO_NAME) {
                throw IllegalStateException("Illegal reassignment of name: $ff_Name to: $ff_Name" )
            }
            name = value
        }
        get() = name

    final override val componentId: CompId
        by lazy { CompId(index, indexedTypeKey) }
    @Suppress("UNCHECKED_CAST")
    final override val indexedType: Class<out SystemComponent>
        get() = indexedTypeKey.subType as Class<out SystemComponent>

    var initialized:Boolean = false
        internal set
    internal  fun _init() {
        init()
        initialized = true
    }
    protected open fun init() {
        initialized = true
    }
    override fun dispose() {
        initialized = false
        super.dispose()
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

    override val indexedTypeKey: IIndexedTypeKey
        get() = componentType().indexedTypeKey

    abstract fun componentType(): ComponentType<out SystemComponent>


    companion object {
        val ASPECT_GROUP = AspectGroup("SystemComponent")
    }

    protected object TypeKeyBuilder {

        private class TypeKey(subType: Class<out SystemComponent>) :
            IndexedTypeKey(SystemComponent::class.java, subType, ASPECT_GROUP)

        fun <T: SystemComponent> create(subType: Class<T>): IIndexedTypeKey =
            Indexer.getOrCreateIndexedTypeKey(
                TypeKey::class.java,
                subType
            ) { TypeKey(subType) }
    }
}