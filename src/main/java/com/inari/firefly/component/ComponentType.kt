package com.inari.firefly.component

import com.inari.util.aspect.Aspect
import com.inari.util.aspect.AspectGroup
import com.inari.util.indexed.IIndexedTypeKey


interface ComponentType<C : Component> : Aspect {

    val indexedTypeKey: IIndexedTypeKey

    override val index: Int
        get() = indexedTypeKey.index
    override val aspectGroup: AspectGroup
        get() = indexedTypeKey.aspectGroup
    override val name: String
        get() = indexedTypeKey.name
}