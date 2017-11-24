package com.inari.firefly.component

import com.inari.commons.lang.indexed.Indexed
import com.inari.commons.lang.indexed.IIndexedTypeKey

class CompId (
    val index: Int,
    val typeKey: IIndexedTypeKey
) : Indexed {
    override fun index(): Int = index
}