package com.inari.firefly.entity.property

interface IValuePropertyAccessor<T> : IVirtualPropertyRef.PropertyAccessor {
    fun get(): T
    fun set(value: T)
}