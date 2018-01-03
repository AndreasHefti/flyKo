package com.inari.firefly.graphics.rendering

import com.inari.commons.geom.PositionF
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

    override final fun indexedTypeKey() = Renderer.typeKey
    companion object : ComponentType<Renderer> {
        override val typeKey = SystemComponent.createTypeKey(Renderer::class.java)
    }



    protected interface TransformDataCollector : TransformData {
        fun set(transform: TransformData)
        fun set(transform: TransformData, xOffset: Float, yOffset: Float)
        fun set(position: PositionF)
        fun add(transform: TransformData)
        fun addOffset(x: Float, y: Float)
    }

    protected class ExactTransformDataCollector internal constructor() : TransformDataCollector {

        override var xOffset: Float = 0f
        override var yOffset: Float = 0f
        override var pivotX: Float = 0f
        override var pivotY: Float = 0f
        override var scaleX: Float = 1f
        override var scaleY: Float = 1f
        override var rotation: Float = 0f

        override fun set(transform: TransformData, xOffset: Float, yOffset: Float) {
            set(transform)
            this.xOffset += xOffset
            this.yOffset += yOffset
        }

        override fun set(position: PositionF) {
            xOffset += position.x
            yOffset += position.y
        }

        override fun set(transform: TransformData) {
            xOffset = transform.xOffset
            yOffset = transform.yOffset
            pivotX = transform.pivotX
            pivotY = transform.pivotY
            scaleX = transform.scaleX
            scaleY = transform.scaleY
            rotation = transform.rotation
        }

        override fun add(transform: TransformData) {
            xOffset += transform.xOffset
            yOffset += transform.yOffset
            pivotX += transform.pivotX
            pivotY += transform.pivotY
            scaleX *= transform.scaleX
            scaleY *= transform.scaleY
            rotation += transform.rotation
        }

        override fun addOffset(x: Float, y: Float) {
            xOffset += x
            yOffset += y
        }

        override val hasRotation: Boolean
            get() = rotation != 0f


        override val hasScale: Boolean
            get() = scaleX != 1f || scaleY != 1f
    }

    protected class DiscreteTransformDataCollector internal constructor() : TransformDataCollector {

        override var xOffset: Float = 0f
        override var yOffset: Float = 0f
        override var pivotX: Float = 0f
        override var pivotY: Float = 0f
        override var scaleX: Float = 0f
        override var scaleY: Float = 0f
        override var rotation: Float = 0f

        override fun set(transform: TransformData, xOffset: Float, yOffset: Float) {
            set(transform)
            this.xOffset = Math.floor((transform.xOffset + xOffset).toDouble()).toFloat()
            this.yOffset = Math.floor((transform.yOffset + yOffset).toDouble()).toFloat()
        }

        override fun set(position: PositionF) {
            this.xOffset = Math.floor((position.x + xOffset).toDouble()).toFloat()
            this.yOffset = Math.floor((position.y + yOffset).toDouble()).toFloat()
        }

        override fun set(transform: TransformData) {
            xOffset = Math.floor(transform.xOffset.toDouble()).toFloat()
            yOffset = Math.floor(transform.yOffset.toDouble()).toFloat()
            pivotX = Math.floor(transform.pivotX.toDouble()).toFloat()
            pivotY = Math.floor(transform.pivotY.toDouble()).toFloat()
            scaleX = transform.scaleX
            scaleY = transform.scaleY
            rotation = transform.rotation
        }

        override fun add(transform: TransformData) {
            xOffset += Math.floor(transform.xOffset.toDouble()).toFloat()
            yOffset += Math.floor(transform.yOffset.toDouble()).toFloat()
            pivotX += Math.floor(transform.pivotX.toDouble()).toFloat()
            pivotY += Math.floor(transform.pivotY.toDouble()).toFloat()
            scaleX *= transform.scaleX
            scaleY *= transform.scaleY
            rotation += transform.rotation
        }

        override fun addOffset(x: Float, y: Float) {
            xOffset += Math.floor(x.toDouble()).toFloat()
            yOffset += Math.floor(y.toDouble()).toFloat()
        }

        override val hasRotation: Boolean
            get() = rotation != 0f

        override val hasScale: Boolean
            get() = scaleX != 1f || scaleY != 1f
    }
}