package com.inari.firefly.component

import com.inari.commons.lang.list.DynArrayRO
import com.inari.commons.lang.list.IntBagRO
import com.inari.firefly.Expr
import com.inari.firefly.IntReceiver
import com.inari.firefly.Predicate
import com.inari.firefly.Receiver
import java.util.*

interface IComponentMap<C : Component> {

    enum class MapAction {
        CREATED, ACTIVATED, DEACTIVATED, DELETED
    }

    val type: ComponentType<C>
    val typeIndex: Int
    val map: DynArrayRO<C>
    val activationMapping: Boolean
    val nameMapping: Boolean

    operator fun contains(index: Int): Boolean
    operator fun contains(name: String): Boolean
    fun idForName(name: String): CompId
    fun indexForName(name: String): Int
    fun activate(index: Int)
    fun deactivate(index: Int)
    fun isActive(index: Int): Boolean
    fun activate(name: String)
    fun deactivate(name: String)
    fun isActive(name: String): Boolean
    operator fun get(index: Int): C
    operator fun get(name: String): C
    fun <CC : C> getAs(index: Int): CC
    fun <CC : C> getAs(name: String): CC
    fun delete(index: Int)
    fun delete(name: String)
    fun deleteAll(predicate: Predicate<C>)
    fun indexIterator(predicate: Predicate<C>): IntReceiver
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