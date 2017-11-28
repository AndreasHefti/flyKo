package com.inari.firefly.control

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.firefly.FFContext
import com.inari.firefly.asset.Asset
import com.inari.firefly.component.ComponentType
import com.inari.firefly.external.FFTimer.UpdateScheduler
import com.inari.firefly.system.component.SystemComponent

abstract class Controller protected constructor() : SystemComponent() {

    var ff_UpdateResolution: Float = -1f

    private val scheduler: UpdateScheduler =
        FFContext.timer.createUpdateScheduler(ff_UpdateResolution)

    override final fun indexedTypeKey(): IIndexedTypeKey = Asset.typeKey



    internal fun processUpdate() {
        if ( ff_UpdateResolution >= 0 ) {
            if (scheduler.needsUpdate()) update()
        } else
            update()
    }

    abstract fun update()

    companion object : ComponentType<Controller> {
        override val typeKey = SystemComponent.createTypeKey(Controller::class.java)
    }

}