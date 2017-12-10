package com.inari.firefly.component

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.commons.lang.indexed.Indexed

class CompId (
    @JvmField val index: Int,
    @JvmField val typeKey: IIndexedTypeKey
) : Indexed {
    override fun index(): Int = index
    override fun toString(): String =
        "CompId(index=$index, typeKey=$typeKey)"

    fun <T> checkType(type: Class<T>): CompId {
        if (typeKey.type<T>() === type)
            return this
        else
            throw IllegalArgumentException("Illegal CompId type: $type")
    }
}