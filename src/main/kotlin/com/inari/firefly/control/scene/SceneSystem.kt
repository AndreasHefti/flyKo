package com.inari.firefly.control.scene

import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.component.ComponentMap
import com.inari.firefly.component.ComponentMapRO
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.Call
import com.inari.util.aspect.Aspects

object SceneSystem : ComponentSystem {

    override val supportedComponents: Aspects =
        SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(Scene)

    private val systemScenes = ComponentSystem.createComponentMapping(
        Scene,
        nameMapping = true,
        activationMapping = true,
        listener = { scene, action -> when (action) {
            //ComponentMap.MapAction.ACTIVATED       -> internalRun(scene.index())
            ComponentMap.MapAction.DEACTIVATED     -> internalStop(scene.index)
            //ComponentMap.MapAction.DELETED         -> internalDelete(scene.index())
            else -> {}
        } }
    )
    val scenes: ComponentMapRO<Scene> = systemScenes


    init {
        FFContext.registerListener(FFApp.UpdateEvent) {
            var i = systemScenes.nextActive(0)
            while(i >= 0) {
                val scene = systemScenes[i]
                if (scene.paused)
                    continue

                scene()
                i = systemScenes.nextActive(i + 1)
            }
        }
        FFContext.loadSystem(this)
    }

    fun runScene(index: Int, callback: Call) {
        if (index !in systemScenes)
            return

        val scene = systemScenes[index]
        scene.callback = callback
        scene.paused = false
        scene.sceneInit()
        systemScenes.activate(index)
    }

    fun pauseScene(index: Int) {
        if (!systemScenes.isActive(index))
            return

        systemScenes[index].paused = true
    }

    fun resumeScene(index: Int) {
        if (!systemScenes.isActive(index))
            return

        systemScenes[index].paused = false
    }

    fun stopScene(index: Int) {
        if (!systemScenes.isActive(index))
            return

        systemScenes.deactivate(index)
    }

    private fun internalStop(index: Int) {
        val scene = systemScenes[index]
        scene.callback()
        if (scene.removeAfterRun)
            systemScenes.delete(scene.index)
        else
            scene.sceneReset()
    }

    override fun clearSystem() {
        systemScenes.clear()
    }
}