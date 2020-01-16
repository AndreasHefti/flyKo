package com.inari.firefly.graphics

import com.inari.firefly.TestApp
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.entity.property.FloatPropertyAccessor
import com.inari.firefly.graphics.view.View
import com.inari.firefly.graphics.view.ViewSystem
import com.inari.firefly.measureTime
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class ETransformTest {

    @Test
    fun testCreation() {
        TestApp
        EntitySystem

        val entityId = Entity.build {
            withComponent(ETransform) {
                ff_View(0)
                ff_Layer(1)
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

        val xposAccessor: FloatPropertyAccessor =
            ETransform.Property.POSITION_X.accessor(EntitySystem[entityId]) as FloatPropertyAccessor
        assertTrue(10f == xposAccessor.get())
        xposAccessor.set(30f)
        assertTrue(30f == xposAccessor.get())
        assertTrue(30f == transform.ff_Position.x)
        assertTrue(30f == transform.data.position.x)

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
            withComponent(ETransform) {
                ff_View("Test")
            }
        }

        val transform = EntitySystem[entityId][ETransform]
        assertTrue(transform.viewIndex != -1)
    }
}