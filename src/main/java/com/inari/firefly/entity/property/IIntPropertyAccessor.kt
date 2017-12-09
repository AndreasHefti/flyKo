package com.inari.firefly.entity.property

interface IIntPropertyAccessor : IVirtualPropertyRef.PropertyAccessor {
    fun get(): Int
    fun set(value: Int)
}