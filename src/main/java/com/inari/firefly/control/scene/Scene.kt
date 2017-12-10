package com.inari.firefly.control.scene

import com.inari.firefly.Call
import com.inari.firefly.NULL_CALL
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent

abstract class Scene protected constructor() : SystemComponent() {

    @JvmField internal var removeAfterRun = false
    @JvmField internal var callback: Call = NULL_CALL

    @JvmField internal var paused = false

    var ff_RemoveAfterRun: Boolean
        get() = removeAfterRun
        set(value) { removeAfterRun = value }
    var ff_Callback: Call
        get() = throw UnsupportedOperationException()
        set(value) { callback = value }

    abstract fun reset()

    internal operator fun invoke() = update()
    protected abstract fun update()

    override final fun indexedTypeKey() = Scene.typeKey
    companion object : ComponentType<Scene> {
        override val typeKey = SystemComponent.createTypeKey(Scene::class.java)
    }
}