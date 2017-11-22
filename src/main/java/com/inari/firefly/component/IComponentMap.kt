package com.inari.firefly.component

interface IComponentMap<C : Component> : Activation {
    fun type(): ComponentType<C>
    fun getId(name: String): Int
    fun activate(name: String)
    fun deactivate(name: String)
    fun isActive(name: String): Boolean
    fun get(id: Int): C
    fun get(name: String): C
    fun delete(id: Int)
    fun delete(name: String)
    fun receiver(): (C) -> C
}