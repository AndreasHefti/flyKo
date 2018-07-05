package com.inari.firefly.component

import com.inari.util.indexed.IIndexedTypeKey
import com.inari.util.indexed.Indexed


class CompId (
    override val index: Int,
    @JvmField val typeKey: IIndexedTypeKey
) : Indexed {
    override fun toString(): String =
        "CompId(index=$index, typeKey=$typeKey)"

    fun checkType(componentType: ComponentType<*>): CompId =
        checkType(componentType.indexedTypeKey.subType)

    fun checkType(type: Class<*>): CompId =
        if (typeKey.subType === type)
            this
        else
            throw IllegalArgumentException("Illegal CompId subType: $type")
}