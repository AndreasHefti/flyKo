package com.inari.firefly.entity

import com.inari.commons.lang.aspect.Aspect
import com.inari.commons.lang.aspect.Aspects
import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.commons.lang.indexed.IndexedTypeSet
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentType
import com.inari.firefly.NO_NAME
import com.inari.firefly.entity.EntityComponent.EntityComponentType
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent

class Entity internal constructor(): SystemComponent() {

    private val components: IndexedTypeSet =
            IndexedTypeSet(EntityComponent.Companion.TypeKey::class.java)

    val aspects: Aspects
        get() = components.aspect

    override fun indexedTypeKey(): IIndexedTypeKey = typeKey

    override fun name(): String {
        if (components.contains(EMeta.typeKey.index())) {
            return components.get<EMeta>(EMeta.typeKey.index()).name()
        }

        return NO_NAME
    }

    fun has(aspect: Aspect): Boolean =
        aspects.contains(aspect)

    fun <C : EntityComponent> get(type: EntityComponentType<C>): C =
            components.get(type.typeKey.index())

    fun <C : EntityComponent> with(cBuilder: EntityComponentBuilder<C>, configure: (C.() -> Unit)): CompId =
            cBuilder.builder( {
                comp: C -> components.set(comp)
                comp
            } )(configure)

    override fun dispose() {
        EntityProvider.dispose(this)
        super.dispose()
    }

    internal fun disposeComponents() {
        if (EntitySystem.entities.isActive(index)) {
            throw IllegalStateException("Entity: $index is still active and cannot be disposed")
        }

        for (aspect in components.aspect) {
            EntityProvider.dispose(components.get<EntityComponent>(aspect.index()))
        }

        components.clear()
    }

    internal fun restore(): Entity {
        setIndex(-1)
        return this
    }

    override fun toString(): String =
            "Entity(name=${name()} " +
            "components=$components)"



    companion object : SingleType<Entity>() {
        override val typeKey = SystemComponent.createTypeKey(Entity::class.java)
        override public fun createEmpty(): Entity = EntityProvider.get()
    }

    abstract class EntityComponentBuilder<C : EntityComponent> : ComponentType<C> {
        private fun doBuild(comp: C, configure: C.() -> Unit, receiver: (C) -> C): CompId {
            comp.also(configure)
            receiver(comp)
            return comp.componentId
        }
        internal fun builder(receiver: (C) -> C): (C.() -> Unit) -> CompId = {
            configure -> doBuild(EntityProvider.getComponent(this), configure, receiver)
        }
        internal abstract fun createEmpty(): C
    }



}