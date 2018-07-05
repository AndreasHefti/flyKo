package com.inari.util.geom

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

import com.inari.commons.geom.Vector2f
import org.junit.Test

class Vector2fTest {

    @Test
    fun testCreation() {
        var v = com.inari.commons.geom.Vector2f()
        assertEquals("1.0", v.dx.toString())
        assertEquals("1.0", v.dy.toString())

        v = com.inari.commons.geom.Vector2f(3.45f, 56.3f)
        assertEquals("3.45", v.dx.toString())
        assertEquals("56.3", v.dy.toString())
    }

    @Test
    fun testFromToConfigString() {
        val v = com.inari.commons.geom.Vector2f(2.33f, 1.1f)
        val configString = v.toConfigString()
        assertEquals("2.33,1.1", configString)
        val v2 = Vector2f()
        v2.fromConfigString(configString)
        assertEquals("2.33", v2.dx.toString())
        assertEquals("1.1", v2.dy.toString())
        assertTrue(v == v2)
    }
}
