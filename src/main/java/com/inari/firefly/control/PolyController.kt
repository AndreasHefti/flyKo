package com.inari.firefly.control

import com.inari.firefly.IntConsumer
import com.inari.firefly.NULL_INT_CONSUMER
import com.inari.firefly.component.CompId
import com.inari.firefly.system.component.SystemComponentSubType
import java.util.*

// NOTE using IntExpr here is because of performance reasons to avoid boxing
// TODO is this the best way to do that?
class PolyController private constructor() : Controller() {

    @JvmField internal val ids: BitSet = BitSet()
    @JvmField internal var controlExpr = NULL_INT_CONSUMER

    var ff_ControlExpr: IntConsumer
        get() = throw UnsupportedOperationException()
        set(value) {controlExpr = setIfNotInitialized(value, "ff_ControlExpr")}

    override fun register(id: CompId)  =
        ids.set(id.instanceId)

    override fun unregister(id: CompId)  =
        ids.set(id.instanceId, false)

    override fun update() {
        val maybe = Optional.ofNullable(null)
        var i: Int = -1
        while (ids.nextSetBit(i++) >= 0)
            controlExpr(i)

    }

    companion object : SystemComponentSubType<Controller, PolyController>(Controller, PolyController::class.java) {
        override fun createEmpty() = PolyController()
    }
}