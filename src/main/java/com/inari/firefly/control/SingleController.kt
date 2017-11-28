package com.inari.firefly.control

import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.FFContext
import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.asset.Asset
import com.inari.firefly.component.CompId
import com.inari.firefly.component.Component
import com.inari.firefly.system.component.SubType

class SingleController(
    var ff_Control: (Component) -> Unit = { _ -> }
) : Controller() {

    var id: CompId = NO_COMP_ID

    override fun update() {
        ff_Control(FFContext.get(id))
    }

    companion object : SubType<SingleController, Controller>() {
        override val typeKey: IndexedTypeKey = Asset.typeKey
        override fun subType(): Class<SingleController> = SingleController::class.java
        override fun createEmpty(): SingleController = SingleController()
    }
}