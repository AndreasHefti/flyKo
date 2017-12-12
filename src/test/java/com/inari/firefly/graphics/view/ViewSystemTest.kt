package com.inari.firefly.graphics.view

import com.inari.firefly.GraphicsMock
import com.inari.firefly.TestApp
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ViewSystemTest {

    @Before
    fun init() {
        ViewSystem.clearSystem()
    }

    @Test
    fun testSystemInit() {
        TestApp
        ViewSystem

        assertNotNull(ViewSystem.baseView)
        assertTrue(GraphicsMock.views.size == 1)
    }
}