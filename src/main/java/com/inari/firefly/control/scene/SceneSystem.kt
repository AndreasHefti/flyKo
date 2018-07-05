package com.inari.firefly.control.scene

import com.inari.firefly.Call
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.component.ComponentMap
import com.inari.firefly.component.ComponentMapRO
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.aspect.Aspects

object SceneSystem : ComponentSystem {

    override val supportedComponents: Aspects =
        SystemComponent.ASPECT_GROUP.createAspects(Scene)

    private val _scenes = ComponentSystem.createComponentMapping(
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
    val scenes: ComponentMapRO<Scene> = _scenes


    init {
        FFContext.registerListener(FFApp.UpdateEvent, object : FFApp.UpdateEvent.Listener {
            override fun invoke() {
                var i = _scenes.nextActive(0)
                while(i >= 0) {
                    val scene = _scenes[i]
                    if (scene.paused)
                        continue

                    scene()
                    i = _scenes.nextActive(i + 1)
                }
            }
        })

        FFContext.loadSystem(this)
    }

    fun runScene(index: Int, callback: Call) {
        if (index !in _scenes)
            return

        val scene = _scenes[index]
        scene.callback = callback
        scene.paused = false
        scene.sceneInit()
        _scenes.activate(index)
    }

    fun pauseScene(index: Int) {
        if (!_scenes.isActive(index))
            return

        _scenes[index].paused = true
    }

    fun resumeScene(index: Int) {
        if (!_scenes.isActive(index))
            return

        _scenes[index].paused = false
    }

    fun stopScene(index: Int) {
        if (!_scenes.isActive(index))
            return

        _scenes.deactivate(index)
    }

    private fun internalStop(index: Int) {
        val scene = _scenes[index]
        scene.callback()
        if (scene.removeAfterRun)
            _scenes.delete(scene.index)
        else
            scene.sceneReset()
    }

    override fun clearSystem() {
        _scenes.clear()
    }
}