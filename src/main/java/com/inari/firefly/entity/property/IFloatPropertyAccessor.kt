package com.inari.firefly.entity.property

interface IFloatPropertyAccessor : IVirtualPropertyRef.PropertyAccessor {
    fun get(): Float
    fun set(value: Float)
}