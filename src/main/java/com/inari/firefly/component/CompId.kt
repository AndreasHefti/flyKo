package com.inari.firefly.component

import com.inari.commons.lang.indexed.Indexed
import com.inari.commons.lang.indexed.IIndexedTypeKey

class CompId (
    @JvmField val index: Int,
    @JvmField val typeKey: IIndexedTypeKey
) : Indexed {
    override fun index(): Int = index
    override fun toString(): String =
        "CompId(index=$index, typeKey=$typeKey)"
}