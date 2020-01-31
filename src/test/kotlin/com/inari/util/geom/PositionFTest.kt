package com.inari.util.geom

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail

import org.junit.Test

class PositionFTest {

    @Test
    fun testPoint() {
        var p1 = PositionF()

        assertTrue(p1.x == 0f)
        assertTrue(p1.y == 0f)

        p1 = PositionF(10, 4)

        assertTrue(p1.x == 10f)
        assertTrue(p1.y == 4f)

        val p2 = PositionF(p1)

        assertTrue(p2.x == 10f)
        assertTrue(p2.y == 4f)

    }

    @Test
    fun testToString() {
        val p1 = PositionF(30, 40)

        assertTrue(p1.x == 30f)
        assertTrue(p1.y == 40f)

        assertEquals("[x=30.0,y=40.0]", p1.toString())
    }

    @Test
    fun testEquality() {
        val p1 = PositionF(30, 40)
        val p2 = PositionF(30, 40)

        assertEquals(p1, p1)
        assertEquals(p1, p2)
        assertEquals(p2, p1)
        assertEquals(p2, p2)

        assertTrue(p1 == p2)
        assertTrue(p2 == p1)

        val p3 = PositionF(40, 30)

        assertFalse(p1 == p3)
        assertFalse(p3 == p1)
        assertFalse(p2 == p3)
        assertFalse(p3 == p2)
    }
}
