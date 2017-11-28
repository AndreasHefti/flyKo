package com.inari.firefly.component

import com.inari.commons.lang.list.DynArrayRO
import com.inari.commons.lang.list.IntBag

typealias MapListener<C> = (C, IComponentMap.MapAction) -> Unit
interface IComponentMap<C : Component> : Activation {

    enum class MapAction {
        CREATED, ACTIVATED, DEACTIVATED, DELETED
    }

    val type: ComponentType<C>
    val typeIndex: Int
    val map: DynArrayRO<C>
    val activationMapping: Boolean
    val nameMapping: Boolean

    fun exists(index: Int): Boolean
    fun exists(name: String): Boolean
    fun idForName(name: String): CompId
    fun indexForName(name: String): Int
    fun activate(name: String)
    fun deactivate(name: String)
    fun isActive(name: String): Boolean
    fun get(index: Int): C
    fun get(name: String): C
    fun delete(index: Int)
    fun delete(name: String)
    fun indexIterator(predicate: (C) -> Boolean): (Int) -> Int
    fun receiver(): (C) -> C
    fun forEach(expr: (C) -> Unit)
    fun forEachActive(expr: (C) -> Unit)
    fun forEachIn(bag: IntBag, expr: (C) -> Unit)
    fun <CC : C> forEachSubtypeIn(bag: IntBag, expr: (CC) -> Unit)

    fun clear()
}