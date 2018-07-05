package com.inari.util.geom

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail

import com.inari.commons.geom.PositionF
import org.junit.Test

class PositionFTest {

    @Test
    fun testPoint() {
        var p1 = com.inari.commons.geom.PositionF()

        assertTrue(p1.x == 0f)
        assertTrue(p1.y == 0f)

        p1 = com.inari.commons.geom.PositionF(10, 4)

        assertTrue(p1.x == 10f)
        assertTrue(p1.y == 4f)

        val p2 = com.inari.commons.geom.PositionF(p1)

        assertTrue(p2.x == 10f)
        assertTrue(p2.y == 4f)

        var p3 = com.inari.commons.geom.PositionF("40,100")

        assertTrue(p3.x == 40f)
        assertTrue(p3.y == 100f)

        try {
            p3 = com.inari.commons.geom.PositionF("40.5,100")
            fail("this should not work and throw an exception!")
        } catch (nfe: NumberFormatException) {
            assertEquals("For input string: \"40.5\"", nfe.message)
        }

    }

    @Test
    fun testFrom() {
        val p1 = com.inari.commons.geom.PositionF()

        assertTrue(p1.x == 0f)
        assertTrue(p1.y == 0f)

        p1.fromConfigString("100,40")

        assertTrue(p1.x == 100f)
        assertTrue(p1.y == 40f)

        try {
            p1.fromConfigString("hey")
            fail("this should not work and throw an exception!")
        } catch (iae: IllegalArgumentException) {
            assertEquals("The stringValue as invalid format: hey", iae.message)
        }

        val p2 = com.inari.commons.geom.PositionF(1, 1)

        p1.setFrom(p2)

        assertTrue(p1.x == 1f)
        assertTrue(p1.y == 1f)
    }

    @Test
    fun testToString() {
        val p1 = com.inari.commons.geom.PositionF(30, 40)

        assertTrue(p1.x == 30f)
        assertTrue(p1.y == 40f)

        assertEquals("[x=30.0,y=40.0]", p1.toString())
    }

    @Test
    fun testEquality() {
        val p1 = com.inari.commons.geom.PositionF(30, 40)
        val p2 = com.inari.commons.geom.PositionF(30, 40)

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
