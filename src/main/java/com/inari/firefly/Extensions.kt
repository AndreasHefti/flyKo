package com.inari.firefly

import com.inari.commons.lang.list.DynArray

inline fun <T> DynArray<T>.forEach(predicate: (T) -> Boolean, consumer: (T) -> Unit) {
    this.forEach({ v ->
        if (v != null && predicate(v)) consumer(v)
    })
}