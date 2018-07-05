package com.inari.firefly.entity

import com.inari.firefly.NO_NAME
import com.inari.firefly.Receiver
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentType
import com.inari.firefly.entity.EntityComponent.Companion.ENTITY_COMPONENT_ASPECTS
import com.inari.firefly.entity.EntityComponent.EntityComponentType
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.aspect.Aspect
import com.inari.util.aspect.Aspects
import com.inari.util.indexed.IndexedTypeSet

class Entity internal constructor(): SystemComponent() {


    @JvmField internal val components: IndexedTypeSet =
        IndexedTypeSet(EntityComponent.Companion.EntityTypeKey::class.java, ENTITY_COMPONENT_ASPECTS)

    val aspects: Aspects
        get() = components.aspect

    override var name: String = NO_NAME
        get() {
            if (components.contains(EMeta.indexedTypeKey.index)) {
                return components.get<EMeta>(EMeta.indexedTypeKey.index).name
            }

            return NO_NAME
        }

    fun has(aspect: Aspect): Boolean =
        aspects.contains(aspect)

    operator fun <C : EntityComponent> get(type: EntityComponentType<C>): C =
        components[type.index]

    fun <C : EntityComponent> with(cBuilder: EntityComponentBuilder<C>, configure: (C.() -> Unit)): CompId =
            cBuilder.builder{ comp ->
                components + comp
                comp
            } (configure)

    internal fun reset() {
        if (EntitySystem.entities.isActive(index)) {
            throw IllegalStateException("Entity: $index is still active and cannot be disposed")
        }

        for (aspect in components.aspect) {
            EntityProvider.dispose(components.get<EntityComponent>(aspect.index))
        }

        components.clear()
        initialized = false
    }

    internal fun restore(): Entity {
        setIndex(-1)
        return this
    }

    override fun toString(): String =
            "Entity(name=$name " +
            "components=$components)"

    override fun componentType(): ComponentType<Entity> =
        Entity.Companion

    companion object : SingleType<Entity>() {
        override val indexedTypeKey by lazy { TypeKeyBuilder.create(Entity::class.java) }
        public override fun createEmpty() = EntityProvider.get()
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

}