package com.inari.firefly.graphics.tile

import com.inari.firefly.BASE_VIEW
import com.inari.firefly.TestApp
import com.inari.firefly.graphics.view.ViewSystem
import org.junit.Test
import kotlin.test.assertEquals

class TileGridSystemTest {

    @Test
    fun testSystemInit() {
        TestApp
        TileGridSystem
    }

    @Test
    fun testSpherical() {
        TestApp
        ViewSystem
        TileGridSystem

        val grid = TileGrid.buildAndGet {
            ff_View(BASE_VIEW)
            ff_Spherical = true
            ff_GridWidth = 3
            ff_GridHeight = 3
        }
        grid[0,0] = 11
        grid[0,1] = 12
        grid[0,2] = 13
        grid[1,0] = 21
        grid[1,1] = 22
        grid[1,2] = 23
        grid[2,0] = 31
        grid[2,1] = 32
        grid[2,2] = 33

        assertEquals(11, grid[0,0])
        assertEquals(31, grid[2,0])
        assertEquals(11, grid[3,0])

        assertEquals(31, grid[-1,0])
        assertEquals(21, grid[-2,0])
        assertEquals(11, grid[-3,0])
        assertEquals(31, grid[-4,0])
    }
}