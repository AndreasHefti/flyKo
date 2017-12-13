package com.inari.firefly.entity.property

import com.inari.firefly.entity.Entity

interface VirtualPropertyRef {

    val propertyName: String
    val type: Class<*>
    fun accessor(entity: Entity): PropertyAccessor

    interface PropertyAccessor

}