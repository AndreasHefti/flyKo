package com.inari.firefly.asset

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.component.CompId
import com.inari.firefly.component.mapping.ComponentMap
import com.inari.firefly.component.mapping.IComponentMap.MapAction
import com.inari.firefly.system.FFContext
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent

object AssetSystem : ComponentSystem {

    private val mapListener: (CompId, MapAction) -> Unit = {
        id, action -> when (action) {
            MapAction.CREATED       -> created(id.index)
            MapAction.ACTIVATED     -> load(id.index)
            MapAction.DEACTIVATED   -> unload(id.index)
            MapAction.DELETED       -> deleted(id.index)
        }
    }

    private val assets: ComponentMap<Asset> = ComponentMap(
        Asset,
        activationMapping = true,
        nameMapping = true,
        listener = mapListener
    )

    init {
        FFContext.registerComponentMapper(assets)
    }

    override val supportedComponents: IAspects = SystemComponent.ASPECT_GROUP.createAspects(
        Asset.typeKey
    )

    private fun created(id: Int) {
        println("AssetSystem: created")
        // TODO
    }

    private fun load(id: Int) {
        println("AssetSystem: load")
        assets.map.get(id).load()
        // TODO
    }

    private fun unload(id: Int) {
        println("AssetSystem: unload")
        assets.map.get(id).unload()
        // TODO
    }

    private fun deleted(id: Int) {
        println("AssetSystem: deleted")
        // TODO
    }

    override fun clearSystem() {
        assets.clear()
    }

    override fun dispose() {
        clearSystem()
        FFContext.disposeComponentMapper(assets)
    }
}