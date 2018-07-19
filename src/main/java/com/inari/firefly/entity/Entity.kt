package com.inari.firefly.entity

import com.inari.firefly.NO_NAME
import com.inari.firefly.component.CompId
import com.inari.firefly.entity.EntityComponent.Companion.ENTITY_COMPONENT_ASPECTS
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentSingleType
import com.inari.util.aspect.Aspect
import com.inari.util.aspect.AspectAware
import com.inari.util.aspect.Aspects
import com.inari.util.aspect.AspectTypeSet

class Entity internal constructor(): SystemComponent(Entity::class.java.name), AspectAware {

    @JvmField internal val components: AspectTypeSet =
        AspectTypeSet(ENTITY_COMPONENT_ASPECTS)

    override val aspects: Aspects
        get() = components.aspects

    override var name: String = NO_NAME
        get() = if (EMeta in components) components.get<EMeta>(EMeta).name else NO_NAME

    fun has(aspect: Aspect): Boolean =
        aspects.contains(aspect)

    operator fun <C : EntityComponent> get(type: EntityComponentType<C>): C =
        components[type]

    fun <C : EntityComponent> with(cBuilder: EntityComponentBuilder<C>, configure: (C.() -> Unit)): CompId =
            cBuilder.builder{ comp ->
                components + comp
                comp
            } (configure)

    internal fun reset() {
        if (EntitySystem.entities.isActive(index))
            throw IllegalStateException("Entity: $index is still active and cannot be disposed")

        for (aspect in components.aspects)
            EntityProvider.dispose(components.get<EntityComponent>(aspect))

        components.clear()
        initialized = false
    }

    internal fun restore(): Entity {
        //setIndex(-1)
        disposeIndex()
        return this
    }

    override fun toString(): String =
            "Entity(name=$name " +
            "components=$components)"

    override fun componentType() =
        Entity.Companion

    companion object : SystemComponentSingleType<Entity>(Entity::class.java) {
        public override fun createEmpty() = EntityProvider.get()
    }
}