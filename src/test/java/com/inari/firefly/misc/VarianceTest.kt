package com.inari.firefly.misc

open class A {}
open class B {}
open class C : A() {}

val type1: Class<in A> = A::class.java
//val type2: Class<in A> = B::class.java
//val type3: Class<in A> = C::class.java

val type11: Class<out A> = A::class.java
//val type21: Class<out A> = B::class.java
val type31: Class<out A> = C::class.java

val type12: Class<in C> = A::class.java
//val type22: Class<in C> = B::class.java
val type32: Class<in C> = C::class.java

//val type113: Class<out C> = A::class.java
//val type213: Class<out C> = B::class.java
val type313: Class<out C> = C::class.java


//class Test<T : A?>
//class TestTest<T : Test>
//
//
//val testA: TestTest<Test<C>> = TestTest()
//val testB = Test<C>()
