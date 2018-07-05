package com.inari.firefly.control

import com.inari.firefly.FFContext
import com.inari.firefly.INFINITE_SCHEDULER
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentType
import com.inari.firefly.external.FFTimer
import com.inari.firefly.system.component.SystemComponent

abstract class Controller protected constructor() : SystemComponent() {

    @JvmField internal var scheduler: FFTimer.Scheduler = INFINITE_SCHEDULER

    var ff_UpdateResolution: Float
        get() = throw UnsupportedOperationException()
        set(value) {scheduler = FFContext.timer.createUpdateScheduler(value) }

    val needsUpdate: Boolean get() =
        scheduler.needsUpdate()

    abstract fun register(id: CompId)
    abstract fun unregister(id: CompId)

    abstract fun update()

    override fun componentType(): ComponentType<Controller> =
        Controller.Companion

    companion object : ComponentType<Controller> {
        override val indexedTypeKey by lazy { TypeKeyBuilder.create(Controller::class.java) }
    }

}