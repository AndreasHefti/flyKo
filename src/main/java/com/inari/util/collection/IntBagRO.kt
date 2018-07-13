package com.inari.util.collection

interface IntBagRO {

    val nullValue: Int
    val expand: Int
    val isEmpty: Boolean
    val size: Int
    val length: Int
    fun isEmpty(index: Int): Boolean
    operator fun contains(value: Int): Boolean
    operator fun get(index: Int): Int
    fun indexOf(value: Int): Int
    operator fun iterator(): IntIterator


}
