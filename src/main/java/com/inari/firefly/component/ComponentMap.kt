package com.inari.firefly.component

import com.inari.commons.lang.list.DynArrayRO
import com.inari.commons.lang.list.IntBagRO
import com.inari.firefly.Expr
import com.inari.firefly.IntFunction
import com.inari.firefly.Predicate
import com.inari.firefly.Receiver

interface ComponentMap<C : Component> : ComponentMapRO<C> {

    enum class MapAction {
        CREATED, ACTIVATED, DEACTIVATED, DELETED
    }

    val type: ComponentType<C>
    val typeIndex: Int
    val map: DynArrayRO<C>
    val activationMapping: Boolean
    val nameMapping: Boolean

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
    fun forEach(expr: Expr<C>)
    fun forEachActive(expr: Expr<C>)
    fun forEachIn(bag: IntBagRO, expr: Expr<C>)
    fun <CC : C> forEachSubtypeIn(bag: IntBagRO, expr: Expr<CC>)
//    TODO
//    fun forEachIn(set: BitSet, expr: Expr<C>)
//    fun <CC : C> forEachSubtypeIn(set: BitSet, expr: Expr<CC>)

    fun clear()
}