package com.inari.firefly.control

import com.inari.firefly.Consumer
import com.inari.firefly.FFContext
import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.NULL_EXPR
import com.inari.firefly.component.CompId
import com.inari.firefly.component.Component
import com.inari.firefly.system.component.SystemComponentSubType

class SingleController private constructor() : Controller() {

    @JvmField internal var id: CompId = NO_COMP_ID
    @JvmField internal var controlExpr: Consumer<Component> = NULL_EXPR()

    var ff_ControlExpr: Consumer<Component>
        get() = throw UnsupportedOperationException()
        set(value) {controlExpr = setIfNotInitialized(value, "ff_ControlExpr")}

    override fun register(id: CompId) {
        this.id = id
    }

    override fun unregister(id: CompId) {
        this.id = NO_COMP_ID
    }

    override fun update() {
        controlExpr(FFContext[id])
    }

    companion object : SystemComponentSubType<Controller, SingleController>(Controller, SingleController::class.java) {
        override fun createEmpty() = SingleController()
    }
}