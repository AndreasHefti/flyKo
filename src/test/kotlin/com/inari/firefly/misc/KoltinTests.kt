package com.inari.firefly.misc

import org.junit.Test
import kotlin.test.assertEquals

class TestClass {

    private var count: Int = 0

    fun count() = ++count

    // anonymous function as value
    val f1: Int
        get() = count

    val f2: Int
        get() {return count}
}

class Test {

    @Test
    fun test1 () {

        var testClass = TestClass()

        assertEquals(0, testClass.f1)
        assertEquals(0, testClass.f2)

        testClass.count()

        assertEquals(1, testClass.f1)
        assertEquals(1, testClass.f2)

        testClass.count()

        assertEquals(2, testClass.f1)
        assertEquals(2, testClass.f2)
    }
}