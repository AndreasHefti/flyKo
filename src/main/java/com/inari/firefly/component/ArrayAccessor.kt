package com.inari.firefly.component

import com.inari.commons.lang.list.DynArray
import com.inari.commons.lang.list.DynArrayRO


class ArrayAccessor<T>(
    private val array: DynArray<T>,
    private val trim: Boolean = false
) {
    val dynArray: DynArrayRO<T> = array
    fun add(t: T) {
        array.add(t)
        if (trim) array.trim()
    }
    fun addAll(a: DynArray<T>) {
        a.addAll(array)
        if (trim) array.trim()
    }
    fun addAll(vararg a: T) {
        a.forEach { t -> array.add(t) }
        if (trim) array.trim()
    }
    fun set(t: T, index: Int) {
        array.set(index, t)
        if (trim) array.trim()
    }
    fun delete(t: T) {
        array.remove(t)
        if (trim) array.trim()
    }
    fun delete(index: Int) {
        array.remove(index)
        if (trim) array.trim()
    }
}