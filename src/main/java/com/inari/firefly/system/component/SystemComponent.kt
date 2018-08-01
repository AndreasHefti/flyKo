package com.inari.firefly.system.component

import com.inari.firefly.component.CompId
import com.inari.firefly.component.NamedComponent
import com.inari.firefly.NO_NAME
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent.Companion.SYSTEM_COMPONENT_ASPECTS
import com.inari.util.aspect.Aspect
import com.inari.util.aspect.AspectType
import com.inari.util.aspect.IndexedAspectType
import com.inari.util.indexed.AbstractIndexed

abstract class SystemComponent protected constructor(
    objectIndexerName: String
) : AbstractIndexed(objectIndexerName), NamedComponent {

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
        by lazy { CompId(index, componentType()) }

    var initialized:Boolean = false
        internal set
    internal fun _init() {
        super.applyNewIndex()
        init()
        initialized = true
    }
    protected open fun init() {
        initialized = true
    }
    override fun dispose() {
        initialized = false
        super.disposeIndex()
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

    abstract fun componentType(): ComponentType<out SystemComponent>

    companion object {
        val SYSTEM_COMPONENT_ASPECTS = IndexedAspectType("SYSTEM_COMPONENT_ASPECTS")
    }

}

abstract class SystemComponentType<C : SystemComponent>(
    final override val typeClass: Class<C>
) : ComponentType<C> {
    val compAspect: Aspect = SYSTEM_COMPONENT_ASPECTS.createAspect(typeClass.simpleName)
    final override val aspectIndex: Int = compAspect.aspectIndex
    final override val aspectName: String = compAspect.aspectName
    final override val aspectType: AspectType = compAspect.aspectType
    override fun toString() = "SystemComponentType:$aspectName"
}

abstract class SystemComponentSingleType<C : SystemComponent>(
    final override val typeClass: Class<C>
) : SystemComponentBuilder<C>(), ComponentType<C> {
    final override val compAspect: Aspect = SYSTEM_COMPONENT_ASPECTS.createAspect(typeClass.simpleName)
    final override val aspectIndex: Int = compAspect.aspectIndex
    final override val aspectName: String = compAspect.aspectName
    final override val aspectType: AspectType = compAspect.aspectType
}

abstract class SystemComponentSubType<C : SystemComponent, CC : C>(
    baseType: SystemComponentType<C>,
    val subTypeClass: Class<CC>
) : SystemComponentBuilder<CC>(), ComponentType<C> {
    override val typeClass: Class<C> = baseType.typeClass
    final override val compAspect: Aspect = baseType.compAspect
    final override val aspectIndex: Int = baseType.aspectIndex
    final override val aspectName: String = baseType.aspectName
    final override val aspectType: AspectType = baseType.aspectType
}
