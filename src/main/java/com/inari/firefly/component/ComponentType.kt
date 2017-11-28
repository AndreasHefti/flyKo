package com.inari.firefly.component

import com.inari.commons.lang.aspect.Aspect
import com.inari.commons.lang.aspect.AspectGroup
import com.inari.commons.lang.indexed.IIndexedTypeKey

interface ComponentType<C : Component> : Aspect {
    val typeKey: IIndexedTypeKey
    fun type(): Class<C> = typeKey.type()
    override fun index(): Int = typeKey.index()
    override fun aspectGroup(): AspectGroup = typeKey.aspectGroup()
    override fun name(): String = typeKey.name()
}