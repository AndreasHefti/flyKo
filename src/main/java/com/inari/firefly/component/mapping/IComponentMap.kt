package com.inari.firefly.component.mapping

import com.inari.firefly.component.Activation
import com.inari.firefly.component.Component
import com.inari.firefly.component.ComponentType

interface IComponentMap<C : Component> : Activation {
    enum class MapAction {
        CREATED, ACTIVATED, DEACTIVATED, DELETED
    }

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