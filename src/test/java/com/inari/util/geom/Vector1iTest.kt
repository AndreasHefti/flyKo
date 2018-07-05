package com.inari.util.geom

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

import com.inari.commons.geom.Vector1i
import org.junit.Test

class Vector1iTest {

    @Test
    fun testCreation() {
        var v = com.inari.commons.geom.Vector1i()
        assertEquals("1", v.d.toString())

        v = com.inari.commons.geom.Vector1i(3)
        assertEquals("3", v.d.toString())
    }

    @Test
    fun testFromToConfigString() {
        val v = com.inari.commons.geom.Vector1i(2)
        val configString = v.toConfigString()
        assertEquals("2", configString)
        val v2 = Vector1i()
        v2.fromConfigString(configString)
        assertEquals("2", v2.d.toString())
        assertTrue(v == v2)
    }
}
