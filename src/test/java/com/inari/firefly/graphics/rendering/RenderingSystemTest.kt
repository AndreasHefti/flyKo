package com.inari.firefly.graphics.rendering

import com.inari.firefly.TestApp
import org.junit.Test
import kotlin.test.assertTrue

class RenderingSystemTest {

    @Test
    fun testSystemInit() {
        TestApp
        RenderingSystem

        assertTrue(
            RenderingSystem.renderer[SimpleSpriteRenderer.index()] === SimpleSpriteRenderer
        )
    }
}