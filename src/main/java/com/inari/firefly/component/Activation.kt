package com.inari.firefly.component

interface Activation {
    fun activate(id: Int)
    fun deactivate(id: Int)
    fun isActive(id: Int): Boolean
}