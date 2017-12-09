package com.inari.firefly.graphics

import com.inari.firefly.TestApp
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.entity.property.IFloatPropertyAccessor
import com.inari.firefly.graphics.view.View
import com.inari.firefly.graphics.view.ViewSystem
import com.inari.firefly.measureTime
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ETransformTest {

    @Test
    fun testCreation() {
        TestApp
        EntitySystem

        val entityId = Entity.build {
            with(ETransform) {
                ff_View.index = 0
                ff_Layer.index = 1
                ff_Position.x = 10f
                ff_Position.y = 30f
                ff_Scale.dx = 2f
            }
        }

        val transform = EntitySystem[entityId][ETransform]
        assertEquals(
            "ETransform(viewRef=0, layerRef=1, position=[x=10.0,y=30.0], pivot=[x=0.0,y=0.0], scale=[dx=2.0,dy=1.0], rot=0.0)",
            transform.toString()
        )

        val xposAccessor: IFloatPropertyAccessor =
            ETransform.Property.POSITION_X.accessor(EntitySystem[entityId]) as IFloatPropertyAccessor
        assertTrue(10f == xposAccessor.get())
        xposAccessor.set(30f)
        assertTrue(30f == xposAccessor.get())
        assertTrue(30f == transform.ff_Position.x)
        assertTrue(30f == transform.xOffset)

        measureTime("accessor preformance", 10000) {
            xposAccessor.set(30f)
        }
    }

    @Test
    fun testViewRef() {
        TestApp
        EntitySystem
        ViewSystem

        View.build {
            ff_Name = "Test"
        }

        val entityId = Entity.build {
            with(ETransform) {
                ff_View.name = "Test"
            }
        }

        val transform = EntitySystem[entityId][ETransform]
        assertTrue(transform.viewIndex != -1)
    }
}