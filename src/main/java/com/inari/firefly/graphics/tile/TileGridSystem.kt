package com.inari.firefly.graphics.tile

import com.inari.commons.lang.aspect.Aspects
import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.component.IComponentMap
import com.inari.firefly.component.IComponentMap.MapAction.CREATED
import com.inari.firefly.component.IComponentMap.MapAction.DELETED
import com.inari.firefly.component.ViewLayerMapping
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityActivationEvent
import com.inari.firefly.external.ViewPortData
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.view.ViewEvent
import com.inari.firefly.graphics.view.ViewEvent.Type.VIEW_DELETED
import com.inari.firefly.graphics.view.ViewLayerAware
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent.Companion.ASPECT_GROUP


object TileGridSystem : ComponentSystem {

    override val supportedComponents: IAspects =
        ASPECT_GROUP.createAspects(TileGrid)

    @JvmField val viewLayerMapping = ViewLayerMapping(TileGrid::class.java)
    @JvmField val grids = ComponentSystem.createComponentMapping(
        TileGrid,
        listener = { grid, action -> when (action) {
            CREATED -> viewLayerMapping.add(grid)
            DELETED -> viewLayerMapping.delete(grid)
            else -> {}
        } }
    )

    init {
        FFContext.registerListener(
            ViewEvent,
            object : ViewEvent.Listener {
                override fun invoke(id: CompId, viewPort: ViewPortData, type: ViewEvent.Type) {
                    when(type) {
                        VIEW_DELETED -> viewLayerMapping[id.index]
                            .forEach { grid -> grids.delete(grid.index()) }
                        else -> {}
                    }
                }
            }
        )

        FFContext.registerListener(
            EntityActivationEvent,
            object : EntityActivationEvent.Listener {
                override fun entityActivated(entity: Entity) =
                    addEntity(entity)
                override fun entityDeactivated(entity: Entity) =
                    removeEntity(entity)
                override fun match(aspects: Aspects): Boolean =
                    aspects.contains(ETile)
            }
        )

        FFContext.loadSystem(this)
    }

    fun exists(viewIndex: Int, layerIndex: Int): Boolean =
        viewIndex in viewLayerMapping && layerIndex in viewLayerMapping[viewIndex]

    fun exists(viewLayer: ViewLayerAware): Boolean =
        exists(viewLayer.viewIndex, viewLayer.layerIndex)

    operator fun get(viewLayer: ViewLayerAware): TileGrid =
        this[viewLayer.viewIndex, viewLayer.layerIndex]

    operator fun get(viewIndex: Int, layerIndex: Int): TileGrid =
        if (exists(viewIndex, layerIndex))
            viewLayerMapping[viewIndex][layerIndex]
        else
            TileGrid.NULL_TILE_GRID


    override fun clearSystem() {
        grids.clear()
    }

    private fun addEntity(entity: Entity) {
        val tileGrid = this[entity[ETransform]]
        val positions = entity[ETile].positions
        val entityId = entity.index()

        var i = 0
        while (i < positions.capacity()) {
            val pos = positions[i++] ?: continue
            tileGrid[pos] = entityId
        }
    }

    private fun removeEntity(entity: Entity) {
        val tileGrid = this[entity[ETransform]]
        val positions = entity[ETile].positions
        val entityId = entity.index()

        var i = 0
        while (i < positions.capacity()) {
            val pos = positions[i++] ?: continue
            tileGrid.resetIfMatch(entityId, pos)
        }
    }
}