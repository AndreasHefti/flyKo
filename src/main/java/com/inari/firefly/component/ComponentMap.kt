package com.inari.firefly.component

import com.inari.firefly.Consumer
import com.inari.firefly.IntFunction
import com.inari.firefly.Predicate
import com.inari.firefly.Receiver
import com.inari.util.collection.DynArrayRO
import com.inari.util.collection.DynIntArrayRO

interface ComponentMap<C : Component> : ComponentMapRO<C> {

    enum class MapAction {
        CREATED, ACTIVATED, DEACTIVATED, DELETED
    }

    val componentType: ComponentType<C>
    val typeIndex: Int
    val map: DynArrayRO<C>
    val activationMapping: Boolean
    val nameMapping: Boolean
    val size: Int

    fun activate(index: Int)
    fun deactivate(index: Int)
    fun activate(name: String)
    fun deactivate(name: String)
    fun activate(id: CompId)
    fun deactivate(id: CompId)
    fun delete(index: Int)
    fun delete(name: String)
    fun delete(id: CompId)
    fun deleteAll(predicate: Predicate<C>)
    fun indexIterator(predicate: Predicate<C>): IntFunction
    fun nextActive(from: Int): Int
    fun receiver(): Receiver<C>
    fun forEach(expr: Consumer<C>)
    fun forEachActive(expr: Consumer<C>)
    fun forEachIn(bag: DynIntArrayRO, expr: Consumer<C>)
    fun <CC : C> forEachSubtypeIn(bag: DynIntArrayRO, expr: Consumer<CC>)
//    TODO
//    fun forEachIn(set: BitSet, expr: Consumer<C>)
//    fun <CC : C> forEachSubtypeIn(set: BitSet, expr: Consumer<CC>)

    fun clear()
}