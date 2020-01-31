package com.inari.firefly.control.scene

import com.inari.firefly.VOID_CALL
import com.inari.firefly.component.ComponentType
import com.inari.firefly.control.trigger.Trigger
import com.inari.firefly.control.trigger.TriggeredSystemComponent
import com.inari.firefly.system.component.SystemComponentType
import com.inari.java.types.BitSet
import com.inari.util.Call

abstract class Scene protected constructor() : TriggeredSystemComponent(Scene::class.java.name) {

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

    fun <A : Trigger> ff_WithRunTrigger(cBuilder: Trigger.Subtype<A>, callback: Call, configure: (A.() -> Unit)): A =
        super.with(cBuilder, { SceneSystem.runScene(index, callback) }, configure)

    fun <A : Trigger> ff_WithStopTrigger(cBuilder: Trigger.Subtype<A>, configure: (A.() -> Unit)): A =
        super.with(cBuilder, stopCall, configure)

    fun <A : Trigger> ff_WithPauseTrigger(cBuilder: Trigger.Subtype<A>, configure: (A.() -> Unit)): A =
        super.with(cBuilder, pauseCall, configure)

    fun <A : Trigger> ff_WithResumeTrigger(cBuilder: Trigger.Subtype<A>, configure: (A.() -> Unit)): A =
        super.with(cBuilder, resumeCall, configure)

    abstract fun sceneInit()
    abstract fun sceneReset()

    internal operator fun invoke() = update()
    protected abstract fun update()

    override fun componentType(): ComponentType<Scene> = Companion
    companion object : SystemComponentType<Scene>(Scene::class.java)
}