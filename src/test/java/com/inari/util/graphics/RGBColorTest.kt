package com.inari.util.graphics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RGBColorTest {

    @Test
    fun testCreation() {
        var color = RGBColor()
        assertEquals("[r=0.0,g=0.0,b=0.0,a=1.0]", color.toString())
        assertTrue(color.hasAlpha)
        color = RGBColor(.5f, .3f, .4f)
        assertEquals("[r=0.5,g=0.3,b=0.4,a=-1.0]", color.toString())
        assertFalse(color.hasAlpha)
        color = RGBColor(.5f, .3f, .4f, .6f)
        assertEquals("[r=0.5,g=0.3,b=0.4,a=0.6]", color.toString())
        // out of range --> range correction
        color = RGBColor(500.5f, -.3f, .4f, .6f)
        assertEquals("[r=1.0,g=0.0,b=0.4,a=0.6]", color.toString())

        color = RGBColor.of(100, 100, 200)
        assertEquals("[r=0.39215687,g=0.39215687,b=0.78431374,a=-1.0]", color.toString())
        // out of range --> range correction
        color = RGBColor.of(100, -100, 500, 20)
        assertEquals("[r=0.39215687,g=0.0,b=1.0,a=0.078431375]", color.toString())

        val color2 = RGBColor(color)
        assertTrue(color2 == color)
        assertFalse(color2 == RGBColor())
    }


    @Test
    fun testRGBA8888() {
        val color = RGBColor(.5f, .3f, .4f, .6f)
        assertEquals("2135713535", color.rgB8888.toString())
        assertEquals("2135713433", color.rgbA8888.toString())
    }

    @Test
    fun testConstants() {
        assertEquals("[r=0.0,g=0.0,b=0.0,a=1.0]", RGBColor.black.toString())
        assertEquals("[r=0.0,g=0.0,b=1.0,a=1.0]", RGBColor.blu.toString())
        assertEquals("[r=0.0,g=1.0,b=0.0,a=1.0]", RGBColor.green.toString())
        assertEquals("[r=1.0,g=0.0,b=0.0,a=1.0]", RGBColor.red.toString())
        assertEquals("[r=1.0,g=1.0,b=1.0,a=1.0]", RGBColor.white.toString())
    }

}
