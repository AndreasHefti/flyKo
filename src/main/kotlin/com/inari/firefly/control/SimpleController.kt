package com.inari.firefly.control

import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.NULL_CALL
import com.inari.firefly.component.CompId
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.util.Call

class SimpleController private constructor() : Controller() {

    @JvmField internal var id: CompId = NO_COMP_ID
    @JvmField internal var controlExpr = NULL_CALL

    var ff_ControlExpr: Call
        get() = throw UnsupportedOperationException()
        set(value) {controlExpr = setIfNotInitialized(value, "ff_ControlExpr")}

    override fun register(id: CompId) {
        this.id = id
    }

    override fun unregister(id: CompId) {
        this.id = NO_COMP_ID
    }

    override fun update() {
        controlExpr()
    }

    companion object : SystemComponentSubType<Controller, SimpleController>(Controller, SimpleController::class.java) {
        override fun createEmpty() = SimpleController()
    }
}