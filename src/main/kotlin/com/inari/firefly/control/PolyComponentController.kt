package com.inari.firefly.control

import com.inari.firefly.NULL_INT_CONSUMER
import com.inari.firefly.component.CompId
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.java.types.BitSet
import com.inari.util.IntConsumer


class PolyComponentController private constructor() : Controller() {

    @JvmField internal val ids: BitSet = BitSet()

    var controlExpr: IntConsumer = NULL_INT_CONSUMER
        set(value) {field = setIfNotInitialized(value, "controlExpr")}

    override fun register(id: CompId)  =
        ids.set(id.instanceId)

    override fun unregister(id: CompId)  =
        ids.set(id.instanceId, false)

    override fun update() {
        var i: Int = -1
        while (ids.nextSetBit(i++) >= 0)
            controlExpr(i)
    }

    companion object : SystemComponentSubType<Controller, PolyComponentController>(Controller, PolyComponentController::class.java) {
        override fun createEmpty() = PolyComponentController()
    }
}