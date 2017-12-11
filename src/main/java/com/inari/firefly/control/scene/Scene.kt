package com.inari.firefly.control.scene

import com.inari.firefly.Call
import com.inari.firefly.Condition
import com.inari.firefly.VOID_CALL
import com.inari.firefly.component.ComponentType
import com.inari.firefly.control.trigger.Trigger
import com.inari.firefly.control.trigger.TriggerSystem
import com.inari.firefly.system.component.SystemComponent
import java.util.*

abstract class Scene protected constructor() : SystemComponent() {

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

    fun ff_createRunTrigger(callback: Call, condition: Condition): Int =
        createTrigger({ SceneSystem.runScene(index, callback) }, condition)

    fun ff_createStopTrigger(condition: Condition): Int =
        createTrigger(stopCall, condition)

    fun ff_createPauseTrigger(condition: Condition): Int =
        createTrigger(pauseCall, condition)

    fun ff_createResumeTrigger(condition: Condition): Int =
        createTrigger(resumeCall, condition)

    fun ff_disposeTrigger(triggerId: Int) {
        if (triggerIds[triggerId]) {
            TriggerSystem.triggers.delete(triggerId)
            triggerIds.set(triggerId, false)
        }
    }

    protected fun run(callback: Call) {
        this.callback = callback
        paused = false
    }

    abstract fun reset()

    internal operator fun invoke() = update()
    protected abstract fun update()

    private fun createTrigger(call: Call, condition: Condition): Int {
        val tId = Trigger.build {
            ff_Condition = condition
            ff_Call = call
        }.index

        triggerIds.set(tId)
        return tId
    }

    override final fun indexedTypeKey() = Scene.typeKey
    companion object : ComponentType<Scene> {
        override val typeKey = SystemComponent.createTypeKey(Scene::class.java)
    }
}