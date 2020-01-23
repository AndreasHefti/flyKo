package com.inari.firefly.control.behavior

import com.inari.firefly.component.CompId
import com.inari.firefly.system.component.SystemComponentBuilder
import com.inari.util.collection.DynIntArray

abstract class BxBranch internal constructor() : BxNode() {

    @JvmField internal val childrenRefs = DynIntArray()

    fun <C : BxNode> ff_WithNode(cBuilder: SystemComponentBuilder<C>, configure: (C.() -> Unit)): CompId {
        val id = cBuilder.build(configure)
        childrenRefs.add(id.instanceId)
        return id
    }
}