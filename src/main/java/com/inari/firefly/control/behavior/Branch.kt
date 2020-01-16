package com.inari.firefly.control.behavior

import com.inari.firefly.component.CompId
import com.inari.firefly.system.component.SystemComponentBuilder
import com.inari.util.collection.DynIntArray

abstract class Branch internal constructor() : BehaviorNode() {

    @JvmField internal val childrenRefs = DynIntArray()

    fun <C : BehaviorNode> ff_WithNode(cBuilder: SystemComponentBuilder<C>, configure: (C.() -> Unit)): CompId {
        val id = cBuilder.build(configure)
        childrenRefs.add(id.instanceId)
        return id
    }
}