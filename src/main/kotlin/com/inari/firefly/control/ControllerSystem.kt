package com.inari.firefly.control

import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityActivationEvent
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import com.inari.util.BitSet
import com.inari.util.aspect.Aspects
import com.inari.util.collection.DynArray
import java.awt.ComponentOrientation

object ControllerSystem : ComponentSystem {

    override val supportedComponents: Aspects = SystemComponent.SYSTEM_COMPONENT_ASPECTS.createAspects(
        Controller
    )

    @JvmField val controller = ComponentSystem.createComponentMapping(
        Controller,
        activationMapping = true,
        nameMapping = true
    )

    init {
        FFContext.registerListener(FFApp.UpdateEvent) {
            controller.forEachActive { c ->
                var i: Int = c.controlled.nextSetBit(0)
                while (i >= 0) {
                    c.update(i)
                    i = c.controlled.nextSetBit(i + 1)
                }
            }
        }
        FFContext.loadSystem(this)
    }

    override fun clearSystem() {
        controller.clear()
    }
}