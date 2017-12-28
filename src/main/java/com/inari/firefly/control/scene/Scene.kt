package com.inari.firefly.control.scene

import com.inari.firefly.Call
import com.inari.firefly.VOID_CALL
import com.inari.firefly.component.ComponentType
import com.inari.firefly.control.trigger.Trigger
import com.inari.firefly.control.trigger.TriggeredSystemComponent
import com.inari.firefly.system.component.SystemComponent
import java.util.*

abstract class Scene protected constructor() : TriggeredSystemComponent() {

    @JvmField internal var removeAfterRun = false
    @JvmField internal var callback: Call = VOID_CALL
    @JvmField internal var paused = false

    private val triggerIds = BitSet()
    private val pauseCall = { SceneSystem.pauseScene(index) }
    private val resumeCall = { SceneSystem.resumeScene(index) }
    private val stopCall = { SceneSystem.stopScene(index) }

    var ff_RemoveAfterRun: Boolean
        get() = removeAfterRun
        set(value) { removeAfterRun = value }

    fun <A : Trigger> withRunTrigger(cBuilder: Trigger.Subtype<A>, callback: Call, configure: (A.() -> Unit)): A =
        super.with(cBuilder, { SceneSystem.runScene(index, callback) }, configure)

    fun <A : Trigger> withStopTrigger(cBuilder: Trigger.Subtype<A>, configure: (A.() -> Unit)): A =
        super.with(cBuilder, stopCall, configure)

    fun <A : Trigger> withPauseTrigger(cBuilder: Trigger.Subtype<A>, configure: (A.() -> Unit)): A =
        super.with(cBuilder, pauseCall, configure)

    fun <A : Trigger> withResumeTrigger(cBuilder: Trigger.Subtype<A>, configure: (A.() -> Unit)): A =
        super.with(cBuilder, resumeCall, configure)

    protected fun run(callback: Call) {
        this.callback = callback
        paused = false
    }

    abstract fun reset()

    internal operator fun invoke() = update()
    protected abstract fun update()

    override final fun indexedTypeKey() = Scene.typeKey
    companion object : ComponentType<Scene> {
        override val typeKey = SystemComponent.createTypeKey(Scene::class.java)
    }
}