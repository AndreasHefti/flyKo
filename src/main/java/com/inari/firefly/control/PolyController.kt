package com.inari.firefly.control

import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.IntExpr
import com.inari.firefly.NULL_INT_EXPR
import com.inari.firefly.component.CompId
import com.inari.firefly.system.component.SubType
import java.util.*

// NOTE using IntExpr here is because of performance reasons to avoid boxing
// TODO is this the best way to do that?
class PolyController private constructor() : Controller() {

    @JvmField internal val ids: BitSet = BitSet()
    @JvmField internal var controlExpr = NULL_INT_EXPR

    var ff_ControlExpr: IntExpr
        get() = throw UnsupportedOperationException()
        set(value) {controlExpr = setIfNotInitialized(value, "ff_ControlExpr")}

    override fun register(id: CompId)  =
        ids.set(id.index)

    override fun unregister(id: CompId)  =
        ids.set(id.index, false)

    override fun update() {
        var i: Int = -1
        while (ids.nextSetBit(i++) >= 0)
            controlExpr(i)
    }

    companion object : SubType<PolyController, Controller>() {
        override val typeKey: IndexedTypeKey = Controller.typeKey
        override fun subType() = PolyController::class.java
        override fun createEmpty() = PolyController()
    }
}