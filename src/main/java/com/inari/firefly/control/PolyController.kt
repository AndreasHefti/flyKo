package com.inari.firefly.control

import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.commons.lang.list.IntBag
import com.inari.firefly.asset.Asset
import com.inari.firefly.system.component.SubType

class PolyController(
    var ff_Control: (Int) -> Unit = { _ -> }
) : Controller() {

    private val ids: IntBag = IntBag(10, -1, 5)

    fun register(index: Int) = ids.add(index)
    fun unregister(index: Int) = ids.remove(index)

    override fun update() {
        val iterator = ids.iterator()
        while (iterator.hasNext()) {
            ff_Control(iterator.next())
        }
    }

    companion object : SubType<PolyController, Controller>() {
        override val typeKey: IndexedTypeKey = Asset.typeKey
        override fun subType(): Class<PolyController> = PolyController::class.java
        override fun createEmpty(): PolyController = PolyController()
    }
}