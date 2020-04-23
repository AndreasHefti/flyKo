package com.inari.firefly.composite

import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentType

abstract class Composite protected constructor() : SystemComponent(Composite::class.java.name) {

    var loaded: Boolean = false
        private set

    internal fun systemLoad() {
        if (!loaded) {
            load()
            loaded = true
        }
    }
    internal fun systemActivate() = activate()
    internal fun systemDeactivate() = deactivate()
    internal fun systemUnload() {
        if (loaded) {
            unload()
            loaded = false
        }
    }

    protected abstract fun load()
    protected abstract fun activate()
    protected abstract fun deactivate()
    protected abstract fun unload()

    override fun componentType() = Companion
    companion object : SystemComponentType<Composite>(Composite::class.java)

}