package com.inari.util.geom

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

import com.inari.commons.geom.Vector2i
import org.junit.Test

class Vector2iTest {

    @Test
    fun testCreation() {
        var v = com.inari.commons.geom.Vector2i()
        assertEquals("1", v.dx.toString())
        assertEquals("1", v.dy.toString())

        v = com.inari.commons.geom.Vector2i(3, 56)
        assertEquals("3", v.dx.toString())
        assertEquals("56", v.dy.toString())
    }

    @Test
    fun testFromToConfigString() {
        val v = com.inari.commons.geom.Vector2i(2, 1)
        val configString = v.toConfigString()
        assertEquals("2,1", configString)
        val v2 = Vector2i()
        v2.fromConfigString(configString)
        assertEquals("2", v2.dx.toString())
        assertEquals("1", v2.dy.toString())
        assertTrue(v == v2)
    }
}
