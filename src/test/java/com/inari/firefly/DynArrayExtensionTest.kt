package com.inari.firefly

import com.inari.commons.lang.list.DynArray
import org.junit.Test

class DynArrayExtensionTest {

    @Test
    fun testPerformance() {

        val dynArray: DynArray<Integer> = DynArray.createTyped(Integer::class.java, 100000)
        for(i: Int in 1..100000)
            dynArray.add(i as Integer)

        var amount = 0
        measureTime("imperative loop", 10000) {
            val size = dynArray.capacity()
            if (size > 0) {
                var i = 0
                while (i < size) {
                    val value = dynArray.get(i++) ?: continue
                    if (value.toInt() % 2 == 0)
                        continue

                    amount += value.toInt()
                }
            }
        }

        val exp: (Integer) -> Unit = { value -> if ( value.toInt() % 2 == 0) amount += value.toInt() }
        amount = 0
        measureTime("conventional forEach with predicate and expression in one function", 10000) {
            dynArray.forEach(
                { value -> if (value.toInt() % 2 == 0) amount += value.toInt() }
            )
        }

        val consumer: (Integer) -> Unit = { value -> amount += value.toInt() }
        val predicate: (Integer) -> Boolean = { value -> value.toInt() % 2 == 0 }
        amount = 0
        measureTime("forEach with separated funcitons for predicate and expression ", 10000) {
            dynArray.forEach(
                { value -> value.toInt() % 2 == 0 },
                { value -> amount += value.toInt() }
            )
        }
    }

}