package com.inari.firefly.misc

import org.junit.Test

interface C1 {
    fun foo(): Int
}

abstract class C2 {
    abstract fun bar(): Int
}

class C1Impl : C1  {

    override fun foo(): Int = C1Impl.foo()

    companion object : C1 {
        override fun foo(): Int = 1
    }
}

@Test
fun test1() {
    val c1 = C1Impl()
    print(c1.foo())
}