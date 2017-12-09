package com.inari.firefly.control.task

import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent

abstract class Task protected constructor() : SystemComponent() {

    protected var removeAfterRun = false

    var ff_RemoveAfterRun: Boolean
        get() = removeAfterRun
        set(value) { removeAfterRun = value }

    override final fun indexedTypeKey() = typeKey

    abstract fun run()

    companion object : ComponentType<Task> {
        override val typeKey = SystemComponent.createTypeKey(Task::class.java)
    }
}