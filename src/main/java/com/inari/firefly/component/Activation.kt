package com.inari.firefly.component

interface Activation {
    fun activate(index: Int)
    fun deactivate(index: Int)
    fun isActive(index: Int): Boolean
}