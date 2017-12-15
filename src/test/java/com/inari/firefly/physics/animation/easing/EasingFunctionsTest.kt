package com.inari.firefly.physics.animation.easing

import org.junit.Test
import kotlin.test.assertEquals

class EasingFunctionsTest {

    private val input = floatArrayOf(
        0.0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f
    )


    @Test
    fun testLinear() {
        val output = input.map { EasingFunctions.linear.calc(it) }
        assertEquals(
            "[0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0]",
            output.toString()
        )
    }

    @Test
    fun testPoly3In() {
        val easingFunction = EasingFunctions.polyIn()
        val output = input.map { "%.3f".format(easingFunction.calc(it)) }
        assertEquals(
            "[0.000, 0.001, 0.008, 0.027, 0.064, 0.125, 0.216, 0.343, 0.512, 0.729, 1.000]",
            output.toString()
        )
    }

    @Test
    fun testPoly25In() {
        val easingFunction = EasingFunctions.polyIn(2.5)
        val output = input.map { "%.6f".format(easingFunction.calc(it)) }
        assertEquals(
            "[0.000000, 0.003162, 0.017889, 0.049295, 0.101193, 0.176777, 0.278855, 0.409963, 0.572433, 0.768433, 1.000000]",
            output.toString()
        )
    }

    @Test
    fun testPoly3Out() {
        val outF = EasingFunctions.polyOut()
        val inF = EasingFunctions.polyIn()
        val output = input.map { "%.3f".format(outF.calc(it)) }
        assertEquals(
            "[0.000, 0.271, 0.488, 0.657, 0.784, 0.875, 0.936, 0.973, 0.992, 0.999, 1.000]",
            output.toString()
        )

        val divOut = input.map { "%.3f".format(outF.calc(it) + inF.calc(1 - it)) }
        assertEquals(
            "[1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000]",
            divOut.toString()
        )
    }

    @Test
    fun testPoly3InOut() {
        val easingFunction = EasingFunctions.polyInOut()
        val output = input.map { "%.3f".format(easingFunction.calc(it)) }
        assertEquals(
            "[0.000, 0.004, 0.032, 0.108, 0.256, 0.500, 0.744, 0.892, 0.968, 0.996, 1.000]",
            output.toString()
        )
    }
}