package com.inari.firefly.component

interface IComponentMap<out C : Component> : Activation {
    val build: (C.() -> Unit) -> Int
    val buildActive: (C.() -> Unit) -> Int
    fun getId(name: String): Int
    fun activate(name: String)
    fun deactivate(name: String)
    fun isActive(name: String): Boolean
    fun get(id: Int): C
    fun get(name: String): C
    fun clear()
}