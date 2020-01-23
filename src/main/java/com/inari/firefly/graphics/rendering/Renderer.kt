package com.inari.firefly.graphics.rendering

import com.inari.util.geom.PositionF
import com.inari.firefly.Consumer
import com.inari.firefly.Predicate
import com.inari.firefly.TRUE_PREDICATE
import com.inari.firefly.component.ComponentType
import com.inari.firefly.entity.Entity
import com.inari.firefly.external.TransformData
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.view.ViewLayerAware
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentType
import com.inari.util.collection.DynArray
import com.inari.util.geom.Rectangle

abstract class Renderer protected constructor(
    private val acceptance: Predicate<Entity> = TRUE_PREDICATE,
    private val sort: Consumer<DynArray<Entity>>? = null
) : SystemComponent(Renderer::class.java.name) {

    @JvmField protected val transformCollector = ExactTransformDataCollector()

    private val entities: DynArray<DynArray<DynArray<Entity>>> =
        DynArray.ofAny(DynArray::class.java)

    fun accept(entity: Entity): Boolean {
        return if (acceptance(entity)) {
            forceGet(entity[ETransform])?.apply {
                add(entity)
                sort?.invoke(this)
            }
            true
        } else
            false
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
        entities[viewLayer.viewIndex]?.get(viewLayer.layerIndex)

    protected fun getIfNotEmpty(viewIndex: Int, layerIndex: Int): DynArray<Entity>? {
        val result = entities[viewIndex]?.get(layerIndex)
        return if (result != null && !result.isEmpty) result
            else null
    }

    private fun forceGet(viewLayer: ViewLayerAware): DynArray<Entity>? =
        forceGet(viewLayer.viewIndex, viewLayer.layerIndex)

    private fun forceGet(viewId: Int, layerId: Int): DynArray<Entity>? {
        if (viewId !in entities)
            entities[viewId] = DynArray.ofAny(DynArray::class.java)
        if (layerId !in entities[viewId]!!)
            entities[viewId]?.set(layerId, DynArray.of(Entity::class.java))

        return entities[viewId]!![layerId]
    }

    abstract fun match(entity: Entity): Boolean

    abstract fun render(viewIndex: Int, layerIndex: Int, clip: Rectangle)

    override fun componentType(): ComponentType<Renderer> = Companion

    companion object : SystemComponentType<Renderer>(Renderer::class.java)

    protected interface TransformDataCollector {
        val data : TransformData
        operator fun invoke(transform: TransformData)
        operator fun invoke(position: PositionF)
        operator fun set(offset: PositionF, transform: TransformData)
        operator fun plus(transform: TransformData)
        operator fun plus(offset: PositionF)
    }

    protected class ExactTransformDataCollector internal constructor() : TransformDataCollector {
        override val data = TransformData()

        override operator fun invoke(transform: TransformData) {
            data.position(transform.position)
            data.pivot(transform.pivot)
            data.scale(transform.scale)
            data.rotation = transform.rotation
        }

        override operator fun invoke(position: PositionF) {
            data.position + position
        }

        override operator fun set(offset: PositionF, transform: TransformData) {
            this(transform)
            data.position + offset
        }

        override operator fun plus(transform: TransformData) {
            data + transform
        }

        override operator fun plus(offset: PositionF) {
            data.position + offset
        }
    }

    protected class DiscreteTransformDataCollector internal constructor() : TransformDataCollector {

        override val data = TransformData()

        override operator fun invoke(transform: TransformData) {
            data.position(
                Math.floor(transform.position.x.toDouble()).toFloat(),
                Math.floor(transform.position.y.toDouble()).toFloat()
            )
            data.pivot(
                Math.floor(transform.pivot.x.toDouble()).toFloat(),
                Math.floor(transform.pivot.y.toDouble()).toFloat()
            )
            data.scale(transform.scale)
            data.rotation = transform.rotation
        }

        override operator fun invoke(position: PositionF) {
            data.position(
                Math.floor(position.x.toDouble()).toFloat(),
                Math.floor(position.y.toDouble()).toFloat()
            )
        }

        override operator fun set(offset: PositionF, transform: TransformData) {
            this(transform)
            data.position(
                Math.floor((transform.position.x + offset.x).toDouble()).toFloat(),
                Math.floor((transform.position.y + offset.y).toDouble()).toFloat()
            )
        }

        override operator fun plus(transform: TransformData) {
            data.position.x += Math.floor(transform.position.x.toDouble()).toFloat()
            data.position.y += Math.floor(transform.position.y.toDouble()).toFloat()
            data.pivot.x += Math.floor(transform.pivot.x.toDouble()).toFloat()
            data.pivot.y += Math.floor(transform.pivot.y.toDouble()).toFloat()
            data.scale.dx *= transform.scale.dx
            data.scale.dy *= transform.scale.dy
            data.rotation += transform.rotation
        }

        override operator fun plus(offset: PositionF) {
            data.position.x += Math.floor(offset.x.toDouble()).toFloat()
            data.position.y += Math.floor(offset.y.toDouble()).toFloat()
        }
    }
}