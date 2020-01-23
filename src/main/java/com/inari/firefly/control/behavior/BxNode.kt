package com.inari.firefly.control.behavior

import com.inari.firefly.OpResult
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentType

abstract class BxNode protected constructor() : SystemComponent(BxNode::class.java.name) {

    abstract fun tick(entityId: Int, behaviour: EBehavior): OpResult

    override fun componentType() = Companion
    companion object : SystemComponentType<BxNode>(BxNode::class.java)
}