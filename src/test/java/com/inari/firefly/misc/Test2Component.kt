package com.inari.firefly.misc

import com.inari.util.geom.Position
import com.inari.commons.lang.list.IntBag
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent

data class Test2Component constructor (
    var ff_Param1: String,
    var ff_Param2: Int,
    var ff_Param3: Position,
    var ff_Param4: IntBag
) : SystemComponent() {

    private constructor() : this("Param1", 0, Position(), IntBag(5, -1, 5))

    override fun componentType(): ComponentType<Test2Component> =
        Test2Component.Companion

    companion object : SingleType<Test2Component>() {
        override val indexedTypeKey by lazy { TypeKeyBuilder.create(Test2Component::class.java) }
        override fun createEmpty(): Test2Component = Test2Component()
    }

}