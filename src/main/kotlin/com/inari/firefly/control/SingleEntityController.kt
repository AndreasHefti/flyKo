package com.inari.firefly.control

import com.inari.firefly.*
import com.inari.firefly.component.CompId
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.IntConsumer

class SingleEntityController private constructor() : Controller() {

    @JvmField internal var id: CompId = NO_COMP_ID
    @JvmField internal var controlExpr = NULL_INT_CONSUMER

    var ff_ControlExpr: IntConsumer
        get() = throw UnsupportedOperationException()
        set(value) {controlExpr = setIfNotInitialized(value, "ff_ControlExpr")}

    override fun register(id: CompId) {
        this.id = id
    }

    override fun unregister(id: CompId) {
        this.id = NO_COMP_ID
    }

    override fun update() {
        controlExpr(id.instanceId)
    }

    companion object : SystemComponentSubType<Controller, SingleEntityController>(Controller, SingleEntityController::class.java) {
        override fun createEmpty() = SingleEntityController()
    }
}