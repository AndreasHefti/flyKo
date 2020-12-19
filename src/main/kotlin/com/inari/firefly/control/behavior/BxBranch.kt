package com.inari.firefly.control.behavior

import com.inari.firefly.component.CompId
import com.inari.firefly.system.component.SystemComponentBuilder
import com.inari.util.collection.DynArray
import com.inari.util.collection.DynIntArray

abstract class BxBranch internal constructor() : BxNode() {

    @JvmField internal val children: DynArray<BxNode> = DynArray.of(5, 5)

    fun <C : BxNode> node(cBuilder: SystemComponentBuilder<C>, configure: (C.() -> Unit)): CompId {
        val id = cBuilder.build(configure)
        children.add(BehaviorSystem.nodes[id.instanceId])
        return id
    }
}