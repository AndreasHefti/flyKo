package com.inari.firefly.entity

import java.util.ArrayDeque
import com.inari.commons.lang.indexed.IndexedTypeSet
import com.inari.commons.lang.list.DynArray


object EntityProvider {

    private val disposedEntities: ArrayDeque<Entity> = ArrayDeque()
    private val disposedComponents: DynArray<ArrayDeque<EntityComponent>> = DynArray.createTyped(ArrayDeque::class.java, 20, 10)

//    fun createForLaterUse(number: Int) {
//        for (i in 0 until number) {
//            disposedEntities.add(
//                    EntitySystem.build {
//
//                    }
//            )
//        }
//    }

}