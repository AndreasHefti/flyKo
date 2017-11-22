package com.inari.firefly.misc

import com.inari.commons.geom.Position
import com.inari.firefly.component.Component
import com.inari.firefly.entity.EMeta
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.system.FFContext


fun main(args: Array<String>) {

    TestComponentSystem
    EntitySystem

    val comp1 = TestComponent.Builder.build {
        ff_Name = "srgfgrg"
        ff_Param2 = 2
    }

    val comp2 = Test2Component.build {
        ff_Name = "dvsvsv"
        ff_Param2 = 3
        ff_Param3 = Position(1, 2)
        ff_Param4.add(1)
        ff_Param4.add(5)
    }

    println(comp1)
    println(comp2)


    println(FFContext.get<Component>(comp1))
    println(FFContext.get<Component>(comp2))
    println(FFContext.get<Component>(comp2).componentId)
    println(FFContext.get<Component>(comp1.typeKey, "srgfgrg"))

    var comp3: TestComponent = FFContext.get(comp1)
    comp3.ff_Name = "445547474574"
    println(comp3)

    StaticTestEvent.send {
        id = comp2
        key = Test2Component.typeKey
    }


    val entityId = Entity.build {
        with(EMeta) {
            ff_Name = "test"
            ff_Controller.add(1)
        }
    }

    println(entityId)
    val entity: Entity = FFContext.get(entityId)
    println(entity)
    println(entity.get(EMeta))



}
