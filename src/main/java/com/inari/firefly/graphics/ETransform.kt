package com.inari.firefly.graphics

import com.inari.commons.geom.PositionF
import com.inari.commons.geom.Vector2f
import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.property.IFloatPropertyAccessor
import com.inari.firefly.entity.property.IVirtualPropertyRef
import com.inari.firefly.external.TransformData
import com.inari.firefly.graphics.view.Layer
import com.inari.firefly.graphics.view.View
import com.inari.firefly.graphics.view.ViewLayerAware
import com.inari.firefly.setFrom

class ETransform private constructor() : EntityComponent(), TransformData, ViewLayerAware {

    @JvmField internal var viewRef = 0
    @JvmField internal var layerRef = 0
    @JvmField internal val position = PositionF(0.0f, 0.0f)
    @JvmField internal val pivot = PositionF(0.0f, 0.0f)
    @JvmField internal val scale = Vector2f(1.0f, 1.0f)
    @JvmField internal var rot = 0.0f

    val ff_View =
        ComponentRefResolver(View, { index-> viewRef = setIfNotInitialized(index, "ff_View") })
    val ff_Layer =
        ComponentRefResolver(Layer, { index-> layerRef = setIfNotInitialized(index, "ff_Layer") })
    var ff_Position: PositionF
        get() = position
        set(value) = position.setFrom(value)
    var ff_Pivot: PositionF
        get() = pivot
        set(value) = pivot.setFrom(value)
    var ff_Scale: Vector2f
        get() = scale
        set(value) { scale.setFrom(value) }
    var ff_Rotation: Float
        get() = rot
        set(value) { rot = value }

    override val xOffset: Float
        get() = position.x
    override val yOffset: Float
        get() = position.y
    override val scaleX: Float
        get() = scale.dx
    override val scaleY: Float
        get() = scale.dy
    override val pivotX: Float
        get() = pivot.x
    override val pivotY: Float
        get() = pivot.y
    override val rotation: Float
        get() = rot
    override val hasRotation: Boolean
        get() = rot != 0.0f
    override val hasScale: Boolean
        get() = scale.dx != 1.0f || scale.dy != 1.0f
    override val viewIndex: Int
        get() = viewRef
    override val layerIndex: Int
        get() = layerRef

    fun move(dx: Float, dy: Float) {
        position.x += dx
        position.y += dy
    }

    override fun reset() {
        viewRef = 0
        layerRef = 0
        position.x = 0.0f
        position.y = 0.0f
        pivot.x = 0.0f
        pivot.y = 0.0f
        scale.dx = 1.0f
        scale.dy = 1.0f
        rot = 0.0f
    }

    override fun toString(): String {
        return "ETransform(viewRef=$viewRef, " +
            "layerRef=$layerRef, " +
            "position=$position, " +
            "pivot=$pivot, " +
            "scale=$scale, " +
            "rot=$rot)"
    }

    private val accessorPosX: IFloatPropertyAccessor = object : IFloatPropertyAccessor {
        override fun set(value: Float) {position.x = value}
        override fun get(): Float = position.x
    }
    private val accessorPosY: IFloatPropertyAccessor = object : IFloatPropertyAccessor {
        override fun set(value: Float) {position.y = value}
        override fun get(): Float = position.y
    }
    private val accessorPivotX: IFloatPropertyAccessor = object : IFloatPropertyAccessor {
        override fun set(value: Float) {pivot.x = value}
        override fun get(): Float = pivot.x
    }
    private val accessorPivotY: IFloatPropertyAccessor = object : IFloatPropertyAccessor {
        override fun set(value: Float) {pivot.y = value}
        override fun get(): Float = pivot.y
    }
    private val accessorScaleX: IFloatPropertyAccessor = object : IFloatPropertyAccessor {
        override fun set(value: Float) {scale.dx = value}
        override fun get(): Float = scale.dx
    }
    private val accessorScaleY: IFloatPropertyAccessor = object : IFloatPropertyAccessor {
        override fun set(value: Float) {scale.dy = value}
        override fun get(): Float = scale.dy
    }
    private val accessorRotation: IFloatPropertyAccessor = object : IFloatPropertyAccessor {
        override fun set(value: Float) {rot = value}
        override fun get(): Float = rot
    }

    enum class Property(
        override val propertyName: String,
        override val type: Class<*>
    ) : IVirtualPropertyRef {
        POSITION_X("position.x", Float::class.java) {
            override fun accessor(entity: Entity): IFloatPropertyAccessor {
                return entity[ETransform].accessorPosX
            }
        },
        POSITION_Y("position.y", Float::class.java) {
            override fun accessor(entity: Entity): IFloatPropertyAccessor {
                return entity[ETransform].accessorPosY
            }
        },
        PIVOT_X("pivot.x", Float::class.java) {
            override fun accessor(entity: Entity): IFloatPropertyAccessor {
                return entity[ETransform].accessorPivotX
            }
        },
        PIVOT_Y("pivot.y", Float::class.java) {
            override fun accessor(entity: Entity): IFloatPropertyAccessor {
                return entity[ETransform].accessorPivotY
            }
        },
        SCALE_X("scale.x", Float::class.java) {
            override fun accessor(entity: Entity): IFloatPropertyAccessor {
                return entity[ETransform].accessorScaleX
            }
        },
        SCALE_Y("scale.y", Float::class.java) {
            override fun accessor(entity: Entity): IFloatPropertyAccessor {
                return entity[ETransform].accessorScaleY
            }
        },
        ROTATION("rotation", Float::class.java) {
            override fun accessor(entity: Entity): IFloatPropertyAccessor {
                return entity[ETransform].accessorRotation
            }
        }
    }

    override fun indexedTypeKey(): IIndexedTypeKey = typeKey
    companion object : EntityComponentType<ETransform>() {
        override val typeKey = EntityComponent.createTypeKey(ETransform::class.java)
        override fun createEmpty() = ETransform()
    }
}