package com.inari.firefly.control

import com.inari.firefly.*
import com.inari.firefly.component.CompId
import com.inari.firefly.external.FFTimer
import com.inari.firefly.system.component.*

abstract class SystemComponentController protected constructor() : SystemComponent(SystemComponentController::class.simpleName!!) {

    @JvmField internal var scheduler: FFTimer.Scheduler = INFINITE_SCHEDULER

    var controlledComponentId: CompId = NO_COMP_ID
        internal set

    var updateResolution: Float
        get() = throw UnsupportedOperationException()
        set(value) { scheduler = FFContext.timer.createUpdateScheduler(value) }

    abstract fun update()

    override fun componentType() = Companion
    companion object : SystemComponentType<SystemComponentController>(SystemComponentController::class)
}