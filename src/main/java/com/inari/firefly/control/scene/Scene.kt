package com.inari.firefly.control.scene

import com.inari.firefly.Call
import com.inari.firefly.Condition
import com.inari.firefly.VOID_CALL
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent
import java.util.*

abstract class Scene protected constructor() : SystemComponent() {

    @JvmField internal var removeAfterRun = false
    @JvmField internal var callback: Call = VOID_CALL
    @JvmField internal var paused = false

    private val triggerIds = BitSet()

    var ff_RemoveAfterRun: Boolean
        get() = removeAfterRun
        set(value) { removeAfterRun = value }
    fun ff_createRunTrigger(callback: Call, condition: Condition): Int {
        val tId = SceneSystem.triggerMap.createTrigger(condition, {
            SceneSystem.runScene(index, callback)
        })
        triggerIds.set(tId)
        return tId
    }
    fun ff_createStopTrigger(condition: Condition): Int {
        val tId = SceneSystem.triggerMap.createTrigger(condition, {
            SceneSystem.stopScene(index)
        })
        triggerIds.set(tId)
        return tId
    }
    fun ff_createPauseTrigger(condition: Condition): Int {
        val tId = SceneSystem.triggerMap.createTrigger(condition, {
            SceneSystem.pauseScene(index)
        })
        triggerIds.set(tId)
        return tId
    }
    fun ff_createResumeTrigger(condition: Condition): Int {
        val tId = SceneSystem.triggerMap.createTrigger(condition, {
            SceneSystem.resumeScene(index)
        })
        triggerIds.set(tId)
        return tId
    }

    fun ff_disposeTrigger(triggerId: Int) {
        if (triggerIds[triggerId]) {
            SceneSystem.triggerMap.disposeTrigger(triggerId)
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

    override final fun indexedTypeKey() = Scene.typeKey
    companion object : ComponentType<Scene> {
        override val typeKey = SystemComponent.createTypeKey(Scene::class.java)
    }
}