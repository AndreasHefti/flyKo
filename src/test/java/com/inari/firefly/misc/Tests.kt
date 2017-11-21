package com.inari.firefly.misc

import com.inari.commons.geom.Position
import com.inari.firefly.entity.EMeta
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem


fun main(args: Array<String>) {


    val comp1 = TestComponentSystem.with(TestComponent).build {
        ff_Name = "srgfgrg"
        ff_Param2 = 2
    }

    val comp2 = TestComponentSystem.comps2.build {
        ff_Name = "dvsvsv"
        ff_Param2 = 3
        ff_Param3 = Position(1, 2)
        ff_Param4.add(1)
        ff_Param4.add(5)
    }

    println(comp1)
    println(comp2)


    println(TestComponentSystem.with(TestComponent).get(comp1))
    println(TestComponentSystem.comps2.get(comp2))
    println(TestComponentSystem.comps2.get(comp2).componentId)
    println(TestComponentSystem.comps1.get("srgfgrg"))

    var comp3: TestComponent = TestComponentSystem.comps1.get(comp1)
    comp3.ff_Name = "445547474574"
    println(comp3)

    StaticTestEvent.send {
        id = comp2
        key = Test2Component.typeKey
    }


    val entityId = EntitySystem.build {
        with(EMeta) {
            ff_Name = "test"
            ff_Controller.add(1)
        }
    }

    println(entityId)
    val entity: Entity = EntitySystem.entities.get(entityId)
    println(entity)
    println(entity.get(EMeta))



}
