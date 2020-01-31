package com.inari.util.geom

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail

import org.junit.Test

class PositionTest {

    @Test
    fun testPoint() {
        var p1 = Position()

        assertTrue(p1.x == 0)
        assertTrue(p1.y == 0)

        p1 = Position(10, 4)

        assertTrue(p1.x == 10)
        assertTrue(p1.y == 4)

        val p2 = Position(p1)

        assertTrue(p2.x == 10)
        assertTrue(p2.y == 4)

    }

    @Test
    fun testToString() {
        val p1 = Position(30, 40)

        assertTrue(p1.x == 30)
        assertTrue(p1.y == 40)

        assertEquals("[x=30,y=40]", p1.toString())
    }

    @Test
    fun testEquality() {
        val p1 = Position(30, 40)
        val p2 = Position(30, 40)

        assertEquals(p1, p1)
        assertEquals(p1, p2)
        assertEquals(p2, p1)
        assertEquals(p2, p2)

        assertTrue(p1 == p2)
        assertTrue(p2 == p1)

        val p3 = Position(40, 30)

        assertFalse(p1 == p3)
        assertFalse(p3 == p1)
        assertFalse(p2 == p3)
        assertFalse(p3 == p2)
    }

}
