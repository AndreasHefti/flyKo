package com.inari.firefly.asset

import com.inari.commons.lang.aspect.IAspects
import com.inari.commons.lang.list.IntBag
import com.inari.firefly.component.IComponentMap
import com.inari.firefly.component.IComponentMap.MapAction
import com.inari.firefly.component.MapListener
import com.inari.firefly.FFContext
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent
import java.util.concurrent.atomic.AtomicInteger


object AssetSystem : ComponentSystem {

    override val supportedComponents: IAspects = SystemComponent.ASPECT_GROUP.createAspects(
        Asset.typeKey
    )

    val assets: IComponentMap<Asset>

    private val noNameCounter: AtomicInteger = AtomicInteger()
    private val dependingAssetIds: IntBag

    init {
        val mapListener: MapListener<Asset> = {
            asset, action -> when (action) {
                MapAction.CREATED       -> created(asset)
                MapAction.ACTIVATED     -> load(asset)
                MapAction.DEACTIVATED   -> unload(asset)
                MapAction.DELETED       -> deleted(asset)
            }
        }

        assets = ComponentSystem.createComponentMapping(
            Asset,
            activationMapping = true,
            nameMapping = true,
            listener = mapListener
        )

        dependingAssetIds = IntBag(1, -1)

        FFContext.loadSystem(this)
    }

    private fun created(asset: Asset) {
        AssetEvent.send(
            id = asset.componentId,
            type = AssetEvent.Type.ASSET_CREATED
        )
    }

    private fun load(asset: Asset) {
        val dependingIndex = asset.dependingIndex()
        if (dependingIndex >= 0 && !assets.isActive(dependingIndex)) {
            assets.activate(dependingIndex)
        }

        asset.load()
        AssetEvent.send(
            id = asset.componentId,
            type = AssetEvent.Type.ASSET_LOADED
        )

    }

    private fun unload(asset: Asset) {
        findDependingAssets(asset.index())
        if (!dependingAssetIds.isEmpty) {
            (0 until dependingAssetIds.length())
                .filterNot { dependingAssetIds.isEmpty(it) }
                .map { dependingAssetIds.get(it) }
                .filter { assets.isActive(it) }
                .forEach { assets.deactivate(it) }
        }

        asset.unload()
        AssetEvent.send(
            id = asset.componentId,
            type = AssetEvent.Type.ASSET_DISPOSED
        )
    }

    private fun deleted(asset: Asset) {
        if (dependingAssetIds.isEmpty) {
            (0 until dependingAssetIds.length())
                .filterNot { dependingAssetIds.isEmpty(it) }
                .forEach { assets.delete(dependingAssetIds.get(it)) }
        }

        AssetEvent.send(
            id = asset.componentId,
            type = AssetEvent.Type.ASSET_DELETED
        )
    }

    override fun clearSystem() {
        assets.clear()
    }

    private fun findDependingAssets(assetId: Int) {
        dependingAssetIds.clear()
        assets.map
            .filter { it.dependsOn(assetId) }
            .forEach { dependingAssetIds.add(it.index()) }
    }
}