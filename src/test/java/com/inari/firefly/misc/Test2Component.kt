package com.inari.firefly.misc

import com.inari.commons.geom.Position
import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.commons.lang.list.IntBag
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentBuilder

data class Test2Component private constructor(
        var ff_Name: String,
        var ff_Param1: String,
        var ff_Param2: Int,
        var ff_Param3: Position,
        var ff_Param4: IntBag
) : SystemComponent() {

    private constructor() : this("NONAME", "Param1", 0, Position(), IntBag(5, -1, 5))

    override fun name(): String = ff_Name
    override fun indexedTypeKey(): IIndexedTypeKey = typeKey

    companion object : SystemComponentBuilder<Test2Component>(), ComponentType<Test2Component> {
        override val typeKey = SystemComponent.createTypeKey(Test2Component::class.java)
        override val subType: Class<Test2Component> = Test2Component.typeKey.type()
        override fun createEmpty(): Test2Component = Test2Component()
    }

}