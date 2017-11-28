package com.inari.firefly

import com.inari.commons.lang.list.DynArray
import com.inari.firefly.graphics.view.ViewSystem
import org.junit.Test

class DynArrayExtensionTest {

    @Test
    fun testPerformance() {

        val dynArray: DynArray<Integer> = DynArray.create(Integer::class.java, 100000)
        for(i: Int in 1..100000)
            dynArray.add(i as Integer)

        var amount = 0
        var time = System.currentTimeMillis()

        for (i in 1..10000) {
            val size = dynArray.capacity()
            if (size > 0) {
                var i = 0
                while (i < size) {
                    val value: Integer = dynArray.get(i++) ?: continue
                    if (value.toInt() % 2 == 0)
                        continue

                    amount += value.toInt()
                }
            }
        }
        println(System.currentTimeMillis() - time)

        val exp: (Integer) -> Unit = { value -> if (value != null && value.toInt() % 2 == 0) amount += value.toInt() }
        amount = 0
        time = System.currentTimeMillis()
        for (i in 1..10000) {
            dynArray.forEach(
                exp
            )
        }
        println(System.currentTimeMillis() - time)

        val consumer: (Integer) -> Unit = { value -> amount += value.toInt() }
        amount = 0
        time = System.currentTimeMillis()
        for (i in 1..10000) {
            dynArray.forEach(
                predicate,
                consumer
            )
        }
        println(System.currentTimeMillis() - time)
    }

    companion object {
        val predicate: (Integer) -> Boolean = { value -> value.toInt() % 2 == 0 }

    }
}