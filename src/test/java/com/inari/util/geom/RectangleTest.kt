package com.inari.util.geom

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

import com.inari.commons.geom.Rectangle
import org.junit.Test

class RectangleTest {

    @Test
    fun testRectangle() {
        var r1 = com.inari.commons.geom.Rectangle()

        assertEquals(r1.x.toLong(), 0)
        assertEquals(r1.y.toLong(), 0)
        assertEquals(r1.width.toLong(), 0)
        assertEquals(r1.height.toLong(), 0)

        r1 = com.inari.commons.geom.Rectangle(10, 10, 100, 200)

        assertEquals(r1.x.toLong(), 10)
        assertEquals(r1.y.toLong(), 10)
        assertEquals(r1.width.toLong(), 100)
        assertEquals(r1.height.toLong(), 200)

        val r2 = com.inari.commons.geom.Rectangle(r1)

        assertEquals(r2.x.toLong(), 10)
        assertEquals(r2.y.toLong(), 10)
        assertEquals(r2.width.toLong(), 100)
        assertEquals(r2.height.toLong(), 200)

        val r3 = com.inari.commons.geom.Rectangle("20,20,300,400")

        assertEquals(r3.x.toLong(), 20)
        assertEquals(r3.y.toLong(), 20)
        assertEquals(r3.width.toLong(), 300)
        assertEquals(r3.height.toLong(), 400)
    }

    @Test
    fun testFrom() {
        val r1 = com.inari.commons.geom.Rectangle()
        r1.fromConfigString("1,1,1,1")

        assertEquals(r1.x.toLong(), 1)
        assertEquals(r1.y.toLong(), 1)
        assertEquals(r1.width.toLong(), 1)
        assertEquals(r1.height.toLong(), 1)

        val r2 = com.inari.commons.geom.Rectangle()
        r2.setFrom(r1)

        assertEquals(r1.x.toLong(), 1)
        assertEquals(r1.y.toLong(), 1)
        assertEquals(r1.width.toLong(), 1)
        assertEquals(r1.height.toLong(), 1)
    }

    @Test
    fun testToString() {
        val r1 = com.inari.commons.geom.Rectangle(30, 20, 111, 444)

        assertEquals(r1.x.toLong(), 30)
        assertEquals(r1.y.toLong(), 20)
        assertEquals(r1.width.toLong(), 111)
        assertEquals(r1.height.toLong(), 444)

        assertEquals("[x=30,y=20,width=111,height=444]", r1.toString())
    }

    fun testEquality() {
        val r1 = com.inari.commons.geom.Rectangle(1, 1, 111, 111)
        val r2 = com.inari.commons.geom.Rectangle(1, 1, 111, 111)

        assertTrue(r1 == r2)
        assertTrue(r2 == r1)

        val r3 = Rectangle(2, 1, 111, 111)

        assertFalse(r1 == r3)
        assertFalse(r3 == r1)
        assertFalse(r2 == r3)
        assertFalse(r3 == r2)
    }
}
