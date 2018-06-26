package com.inari.firefly.graphics.rendering

import com.inari.util.geom.PositionF
import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.list.DynArray
import com.inari.firefly.Expr
import com.inari.firefly.Predicate
import com.inari.firefly.TRUE_PREDICATE
import com.inari.firefly.component.ComponentType
import com.inari.firefly.entity.Entity
import com.inari.firefly.external.TransformData
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.view.ViewLayerAware
import com.inari.firefly.system.component.SystemComponent

abstract class Renderer protected constructor(
    private val acceptance: Predicate<Entity> = TRUE_PREDICATE(),
    private val sort: Expr<DynArray<Entity>>? = null
) : SystemComponent() {

    @JvmField protected val transformCollector = ExactTransformDataCollector()

    private val entities: DynArray<DynArray<DynArray<Entity>>> =
        DynArray.createTyped(DynArray::class.java)

    fun accept(entity: Entity): Boolean {
        return if (acceptance(entity)) {
            val renderable = forceGet(entity[ETransform])
            renderable.add(entity)
            sort?.invoke(renderable)
            true
        } else {
            false
        }
    }

    fun dispose(entity: Entity) {
        if (acceptance(entity))
            this[entity[ETransform]]?.remove(entity)
    }

    internal fun clearView(viewIndex: Int) =
        entities.remove(viewIndex)

    internal fun clearLayer(viewIndex: Int, layerIndex: Int) =
        entities[viewIndex]?.remove(layerIndex)

    protected operator fun get(viewLayer: ViewLayerAware): DynArray<Entity>? =
        entities.get(viewLayer.viewIndex)?.get(viewLayer.layerIndex)

    protected fun getIfNotEmpty(viewIndex: Int, layerIndex: Int): DynArray<Entity>? {
        val result = entities.get(viewIndex)?.get(layerIndex)
        return if (result != null && !result.isEmpty) result
            else null
    }

    private fun forceGet(viewLayer: ViewLayerAware): DynArray<Entity> =
        forceGet(viewLayer.viewIndex, viewLayer.layerIndex)

    private fun forceGet(viewId: Int, layerId: Int): DynArray<Entity> {
        if (viewId !in entities) {
            entities.set(viewId, DynArray.createTyped(DynArray::class.java))
        }
        if (layerId !in entities[viewId]) {
            entities[viewId].set(layerId, DynArray.create(Entity::class.java))
        }

        return entities[viewId][layerId]
    }

    abstract fun match(entity: Entity): Boolean

    abstract fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle)

    final override fun indexedTypeKey() = Renderer.typeKey
    companion object : ComponentType<Renderer> {
        override val typeKey = SystemComponent.createTypeKey(Renderer::class.java)
    }

    protected interface TransformDataCollector {
        val data : TransformData
        fun set(transform: TransformData)
        fun set(transform: TransformData, xOffset: Float, yOffset: Float)
        fun set(position: PositionF)
        fun add(transform: TransformData)
        fun addOffset(x: Float, y: Float)
    }

    protected class ExactTransformDataCollector internal constructor() : TransformDataCollector {
        override val data = TransformData()

        override fun set(transform: TransformData, xOffset: Float, yOffset: Float) {
            set(transform)
            data.position.x += xOffset
            data.position.y += yOffset
        }

        override fun set(position: PositionF) {
            data.position.x = position.x
            data.position.y = position.y
        }

        override fun set(transform: TransformData) {
            data.position.x = transform.position.x
            data.position.y = transform.position.y
            data.pivot.x = transform.pivot.x
            data.pivot.y = transform.pivot.y
            data.scale.dx = transform.scale.dx
            data.scale.dy = transform.scale.dy
            data.rotation = transform.rotation
        }

        override fun add(transform: TransformData) {
            data.position.x += transform.position.x
            data.position.y += transform.position.y
            data.pivot.x += transform.pivot.x
            data.pivot.y += transform.pivot.y
            data.scale.dx *= transform.scale.dx
            data.scale.dy *= transform.scale.dy
            data.rotation += transform.rotation
        }

        override fun addOffset(x: Float, y: Float) {
            data.position.x += x
            data.position.y += y
        }
    }

    protected class DiscreteTransformDataCollector internal constructor() : TransformDataCollector {

        override val data = TransformData()

        override fun set(transform: TransformData, xOffset: Float, yOffset: Float) {
            set(transform)
            data.position.x = Math.floor((transform.position.x + xOffset).toDouble()).toFloat()
            data.position.y = Math.floor((transform.position.y + yOffset).toDouble()).toFloat()
        }

        override fun set(position: PositionF) {
            data.position.x = Math.floor((position.x).toDouble()).toFloat()
            data.position.y = Math.floor((position.y).toDouble()).toFloat()
        }

        override fun set(transform: TransformData) {
            data.position.x = Math.floor(transform.position.x.toDouble()).toFloat()
            data.position.y = Math.floor(transform.position.y.toDouble()).toFloat()
            data.pivot.x = Math.floor(transform.pivot.x.toDouble()).toFloat()
            data.pivot.y = Math.floor(transform.pivot.y.toDouble()).toFloat()
            data.scale.dx = transform.scale.dx
            data.scale.dy = transform.scale.dy
            data.rotation = transform.rotation
        }

        override fun add(transform: TransformData) {
            data.position.x += Math.floor(transform.position.x.toDouble()).toFloat()
            data.position.y += Math.floor(transform.position.y.toDouble()).toFloat()
            data.pivot.x += Math.floor(transform.pivot.x.toDouble()).toFloat()
            data.pivot.y += Math.floor(transform.pivot.y.toDouble()).toFloat()
            data.scale.dx *= transform.scale.dx
            data.scale.dy *= transform.scale.dy
            data.rotation += transform.rotation
        }

        override fun addOffset(x: Float, y: Float) {
            data.position.x += Math.floor(x.toDouble()).toFloat()
            data.position.y += Math.floor(y.toDouble()).toFloat()
        }
    }
}