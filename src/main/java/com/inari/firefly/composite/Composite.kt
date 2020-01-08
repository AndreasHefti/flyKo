package com.inari.firefly.composite

import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentType

abstract class Composite protected constructor() : SystemComponent(Composite::class.java.name) {

    var loaded: Boolean = false
        protected set

    internal fun systemLoad() {
        if (!loaded)
            load()
    }
    internal fun systemActivate() = activate()
    internal fun systemDeactivate() = deactivate()
    internal fun systemUnload() {
        if (loaded)
            unload()
    }

    protected abstract fun load()
    protected abstract fun activate()
    protected abstract fun deactivate()
    protected abstract fun unload()

    override fun componentType() = Companion
    companion object : SystemComponentType<Composite>(Composite::class.java)

}