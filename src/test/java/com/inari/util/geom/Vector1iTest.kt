package com.inari.util.geom

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

import org.junit.Test

class Vector1iTest {

    @Test
    fun testCreation() {
        var v = Vector1i()
        assertEquals("1", v.d.toString())

        v = Vector1i(3)
        assertEquals("3", v.d.toString())
    }

}
