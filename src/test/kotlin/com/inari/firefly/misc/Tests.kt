package com.inari.firefly.misc

import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.TRUE_SUPPLIER
import com.inari.firefly.TestApp
import com.inari.firefly.asset.AssetSystem
import com.inari.firefly.control.ControllerSystem
import com.inari.firefly.control.PolyComponentController
import com.inari.firefly.control.task.EntityTask
import com.inari.firefly.control.trigger.UpdateEventTrigger
import com.inari.firefly.physics.animation.easing.EasedProperty
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.firefly.entity.EMeta
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.entity.EMultiplier
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.util.IntConsumer


fun main(args: Array<String>) {

    TestApp
    TestComponentSystem
    EntitySystem
    AssetSystem
    ControllerSystem
//
//    val comp1 = TestComponent.build {
//        Name = "srgfgrg"
//        Param2 = 2
//    }
//
//    val comp2 = Test2Component.build {
//        Name = "dvsvsv"
//        Param2 = 3
//        Param3 = Position(1, 2)
//        Param4.add(1)
//        Param4.add(5)
//    }
//
//    println(comp1)
//    println(comp2)
//
//
//    println(FFContext.get<Component>(comp1))
//    println(FFContext.get<Component>(comp2))
//    println(FFContext.get<Component>(comp2).componentId)
//    println(FFContext.get(TestComponent, "srgfgrg"))
//
//    var comp3: TestComponent = FFContext.get(comp1)
//    try {
//        comp3.Name = "445547474574"
//        println("prevent name reassignment failed")
//    } catch (e: Exception) {
//        println("prevent name reassignment ok")
//    }
//    println(comp3)
//
//    StaticTestEvent.send {
//        id = comp2
//    }
//
//
//    val entityId = Entity.build {
//        with(EMeta) {
//            Name = "test"
//            Controller.add(1)
//        }
//    }
//
//    println(entityId)
//    val entity: Entity = FFContext.get(entityId)
//    println(entity)
//    println(entity.name())
//    println(entity.get(EMeta))
//    println(FFContext.get("test", EMeta))
//
//
//
//    val propTest = object : PropTest(){}
//    propTest.loaded
//
//
//    TestAsset.build {
//        Name = "testAsset"
//        Param1 = "testParam1"
//        Param2 = 3f
//    }
//    FFContext
//        .activate(Asset, "testAsset")
//        .deactivate(Asset, "testAsset")
//
//
//    val meta = EMeta.createEmpty()
//    println(meta.componentId)
//    println(meta.componentId)
//
//
//    val propTest2 = PropTest2()
//    propTest2.mutable = "********** 1"
//    println(propTest2.getMutable)
//    propTest2.mutable = "********** 2"
//    println(propTest2.getMutable)
//
//    TestApp
//    FFApp.eventDispatcher
//    FFApp.eventDispatcher
//
//    PropsTest3
//    PropsTest3.prop
//    PropsTest3.prop
//
    val testControl: IntConsumer = { i -> println(i) }

//
    PolyComponentController.build {
        name = "test"
        updateResolution = 1f
        controlExpr = testControl
    }

    Entity.build {
        component(ETransform) {
            position.x = 1f
        }
        component(EAnimation) {
            animation(EasedProperty) {
                looping = true
                propertyRef = ESprite.Property.TINT_ALPHA
            }
        }
        component(EMultiplier) {
            positions.addAll(1f, 3f)
        }
        component(EMeta) {
            controller("sofbno")
        }
    }

    EntityTask.build {
        name = ""
        trigger(UpdateEventTrigger, NO_COMP_ID) {
            condition = TRUE_SUPPLIER
            disposeAfter = true
        }
    }


}
