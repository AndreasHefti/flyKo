package com.inari.firefly.misc

import com.inari.util.geom.Position
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentSingleType
import com.inari.util.collection.DynIntArray

data class Test2Component constructor (
    var ff_Param1: String,
    var ff_Param2: Int,
    var ff_Param3: Position,
    var ff_Param4: DynIntArray
) : SystemComponent(Test2Component::class.java.name) {

    private constructor() : this("Param1", 0, Position(), DynIntArray(5, -1, 5))

    override fun componentType() =
        Test2Component.Companion

    companion object : SystemComponentSingleType<Test2Component>(Test2Component::class.java) {
        override fun createEmpty(): Test2Component = Test2Component()
    }

}