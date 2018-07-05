package com.inari.util.geom

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail

import com.inari.commons.geom.Position
import org.junit.Test

class PositionTest {

    @Test
    fun testPoint() {
        var p1 = com.inari.commons.geom.Position()

        assertTrue(p1.x == 0)
        assertTrue(p1.y == 0)

        p1 = com.inari.commons.geom.Position(10, 4)

        assertTrue(p1.x == 10)
        assertTrue(p1.y == 4)

        val p2 = com.inari.commons.geom.Position(p1)

        assertTrue(p2.x == 10)
        assertTrue(p2.y == 4)

        var p3 = com.inari.commons.geom.Position("40,100")

        assertTrue(p3.x == 40)
        assertTrue(p3.y == 100)

        try {
            p3 = com.inari.commons.geom.Position("40.5,100")
            fail("this should not work and throw an exception!")
        } catch (nfe: NumberFormatException) {
            assertEquals("For input string: \"40.5\"", nfe.message)
        }

    }

    @Test
    fun testFrom() {
        val p1 = com.inari.commons.geom.Position()

        assertTrue(p1.x == 0)
        assertTrue(p1.y == 0)

        p1.fromConfigString("100,40")

        assertTrue(p1.x == 100)
        assertTrue(p1.y == 40)

        try {
            p1.fromConfigString("hey")
            fail("this should not work and throw an exception!")
        } catch (iae: IllegalArgumentException) {
            assertEquals("The stringValue as invalid format: hey", iae.message)
        }

        val p2 = com.inari.commons.geom.Position(1, 1)

        p1.setFrom(p2)

        assertTrue(p1.x == 1)
        assertTrue(p1.y == 1)
    }

    @Test
    fun testToString() {
        val p1 = com.inari.commons.geom.Position(30, 40)

        assertTrue(p1.x == 30)
        assertTrue(p1.y == 40)

        assertEquals("[x=30,y=40]", p1.toString())
    }

    @Test
    fun testEquality() {
        val p1 = com.inari.commons.geom.Position(30, 40)
        val p2 = com.inari.commons.geom.Position(30, 40)

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
