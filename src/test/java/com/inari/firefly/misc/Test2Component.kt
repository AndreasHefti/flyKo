package com.inari.firefly.misc

import com.inari.commons.geom.Position
import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.commons.lang.list.IntBag
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent

data class Test2Component private constructor (
    var ff_Param1: String,
    var ff_Param2: Int,
    var ff_Param3: Position,
    var ff_Param4: IntBag
) : SystemComponent() {

    private constructor() : this("Param1", 0, Position(), IntBag(5, -1, 5))

    override fun indexedTypeKey(): IIndexedTypeKey = typeKey

    companion object : SingleType<Test2Component>() {
        override val typeKey = SystemComponent.createTypeKey(Test2Component::class.java)
        override fun createEmpty(): Test2Component = Test2Component()
    }

}