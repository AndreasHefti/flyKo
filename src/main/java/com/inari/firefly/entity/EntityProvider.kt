package com.inari.firefly.entity

import com.inari.commons.lang.list.DynArray
import com.inari.firefly.entity.Entity.EntityComponentBuilder
import java.util.*


object EntityProvider {

    private val disposedEntities: ArrayDeque<Entity> = ArrayDeque()
    private val disposedComponents: DynArray<ArrayDeque<EntityComponent>> = DynArray.createTyped(ArrayDeque::class.java, 20, 10)

    fun createEntityForLaterUse(number: Int) {
        for (i in 0 until number) {
            disposedEntities.add(Entity.createEmpty())
        }
    }

    fun createComponentForLaterUse(number: Int, builder: EntityComponentBuilder<*>) {
        val cache = getOrCreate(builder.typeKey.index())
        for (i in 0 until number) {
            cache.add(builder.createEmpty())
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <C : EntityComponent> getComponent(builder: EntityComponentBuilder<C>): C {
        val cache = getOrCreate(builder.typeKey.index())
        return if (cache.isEmpty()) {
            builder.createEmpty()
        } else {
            cache.pop() as C
        }
    }

    fun get(): Entity {
        return if (disposedEntities.isEmpty()) {
            Entity()
        } else {
            disposedEntities.pop().restore()
        }
    }

    fun dispose(entity: Entity) {
        val entityId = entity.index()
        if (EntitySystem.entities.isActive(entityId)) {
            throw IllegalStateException("Entity: $entityId is still active and cannot be disposed")
        }

        entity.disposeComponents()
        disposedEntities.add(entity)
    }

    fun dispose(entityComponent: EntityComponent) {
        entityComponent.reset()
        getOrCreate(entityComponent.index()).add(entityComponent)
    }

    private fun getOrCreate(index: Int): ArrayDeque<EntityComponent> {
        if (!disposedComponents.contains(index)) {
            disposedComponents.set(index, ArrayDeque())
        }
        return disposedComponents.get(index)
    }

}