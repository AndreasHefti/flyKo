package com.inari.firefly.control.behavior

import com.inari.firefly.entity.Entity
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentType
import com.inari.util.OpResult

abstract class BxNode protected constructor() : SystemComponent(BxNode::class.java.name) {

    abstract fun tick(entity: Entity, behavior: EBehavior): OpResult

    override fun componentType() = Companion
    companion object : SystemComponentType<BxNode>(BxNode::class.java)
}