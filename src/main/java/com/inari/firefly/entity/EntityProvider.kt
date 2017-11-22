package com.inari.firefly.entity

import com.inari.commons.lang.list.DynArray
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
    fun <C : EntityComponent> getCompoent(builder: EntityComponentBuilder<C>): C {
        val cache = getOrCreate(builder.typeKey.index())
        return if (cache.isEmpty()) {
            builder.createEmpty()
        } else {
            cache.pop() as C
        }

    }

    fun dispose(entity: Entity) {
        // TODO
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