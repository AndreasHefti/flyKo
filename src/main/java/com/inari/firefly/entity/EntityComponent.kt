package com.inari.firefly.entity

import com.inari.firefly.Receiver
import com.inari.firefly.component.CompId
import com.inari.firefly.component.Component
import com.inari.firefly.component.ComponentType
import com.inari.firefly.entity.EntityComponent.Companion.ENTITY_COMPONENT_ASPECTS
import com.inari.util.aspect.Aspect
import com.inari.util.aspect.AspectType
import com.inari.util.aspect.IndexedAspectType

abstract class EntityComponent protected constructor() : Component {
    
    /** An EntityComponent instance has no object index (-1) only a subType index
     *  supported by the index subType key
     */
    final override val componentId: CompId
        by lazy { CompId(-1, componentType()) }

    final override fun dispose() =
        EntityProvider.dispose(this)

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

    abstract fun componentType(): ComponentType<out EntityComponent>

    override val index: Int
        get() = componentType().aspectIndex
    override val indexedTypeName: String
        get() = ENTITY_COMPONENT_ASPECTS.name

    companion object {
        val ENTITY_COMPONENT_ASPECTS = IndexedAspectType("ENTITY_COMPONENT_ASPECTS")
    }
}

abstract class EntityComponentBuilder<C : EntityComponent> : ComponentType<C> {
    private fun doBuild(comp: C, configure: C.() -> Unit, receiver: (C) -> C): CompId {
        comp.also(configure)
        comp._init()
        receiver(comp)
        return comp.componentId
    }
    internal fun builder(receiver: Receiver<C>): (C.() -> Unit) -> CompId = {
        configure -> doBuild(EntityProvider.getComponent(this), configure, receiver)
    }
    internal fun create(): C = createEmpty()
    protected abstract fun createEmpty(): C
}

abstract class EntityComponentType<C : EntityComponent>(
    final override val typeClass: Class<out EntityComponent>
) : EntityComponentBuilder<C>(), ComponentType<C> {
    val compAspect: Aspect = ENTITY_COMPONENT_ASPECTS.createAspect(typeClass.simpleName)
    final override val aspectIndex: Int = compAspect.aspectIndex
    final override val aspectName: String = compAspect.aspectName
    final override val aspectType: AspectType = compAspect.aspectType
}