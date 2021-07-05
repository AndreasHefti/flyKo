package com.inari.firefly.control

import com.inari.firefly.FFContext
import com.inari.firefly.INFINITE_SCHEDULER
import com.inari.firefly.external.FFTimer
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentSingleType
import com.inari.firefly.system.component.SystemComponentType

abstract class EntityController private constructor() : SystemComponent("EntityController") {

    @JvmField internal var scheduler: FFTimer.Scheduler = INFINITE_SCHEDULER

    var updateResolution: Float
        get() = throw UnsupportedOperationException()
        set(value) { scheduler = FFContext.timer.createUpdateScheduler(value) }

    abstract fun update(componentId: Int)

    override fun componentType() = Companion
    companion object : SystemComponentType<EntityController>(EntityController::class)
}