package com.inari.firefly.system.component

import com.inari.commons.lang.aspect.AspectGroup
import com.inari.commons.lang.indexed.*
import com.inari.firefly.component.CompId
import com.inari.firefly.component.NamedComponent
import com.inari.firefly.NO_NAME

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

    final override val componentId: CompId = CompId(index, indexedTypeKey())
    final override fun indexedObjectType(): Class<out IndexedObject> = indexedTypeKey().type<IndexedObject>()

    var initialized:Boolean = false
        internal set
    internal  fun _init() {
        init()
        initialized = true
    }
    open protected fun init() {
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

    companion object {

        val SYSTEM_COMPONENT_ASPECTS = AspectGroup("SystemComponent")

        fun <T: SystemComponent> createTypeKey(type: Class<T>): IndexedTypeKey = Indexer.createIndexedTypeKey(TypeKey::class.java, type)

        class TypeKey<out C : SystemComponent> private constructor(indexedType: Class<C>) : IndexedTypeKey(indexedType) {
            override fun aspectGroup(): AspectGroup = SYSTEM_COMPONENT_ASPECTS
            override fun baseType(): Class<SystemComponent> = SystemComponent::class.java
            @Suppress("UNCHECKED_CAST") fun baseComponentType(): Class<out C> = indexedType as Class<out C>
            override fun toString(): String = "SystemComponent:" + type<C>().simpleName
        }
    }
}