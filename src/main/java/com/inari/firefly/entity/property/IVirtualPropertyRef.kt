package com.inari.firefly.entity.property

import com.inari.firefly.entity.Entity

interface IVirtualPropertyRef {

    val propertyName: String
    val type: Class<*>
    fun accessor(entity: Entity): PropertyAccessor

    interface PropertyAccessor

}