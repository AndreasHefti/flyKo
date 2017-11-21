package com.inari.firefly.component

import com.inari.commons.lang.indexed.Indexed
import com.inari.commons.lang.indexed.IIndexedTypeKey

data class CompId(
        private val index: Int,
        private val typeKey: IIndexedTypeKey
) : Indexed {
    override fun index(): Int = index
    fun typeKey(): IIndexedTypeKey = typeKey
}