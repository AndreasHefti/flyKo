package com.inari.firefly.misc

import com.inari.firefly.measureTime
import com.inari.util.collection.DynArray
import org.junit.Test

class DynArrayExtensionTest {

    @Test
    fun testPerformance() {

        val dynArray: DynArray<Integer> = DynArray.of(Integer::class.java, 100000)
        for(i: Int in 1..100000)
            dynArray.add(i as Integer)

        var amount = 0
        measureTime("imperative loop", 10000) {
            val size = dynArray.capacity
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

    }

}