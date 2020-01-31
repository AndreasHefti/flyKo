package com.inari.firefly

import com.inari.util.geom.PositionF
import com.inari.firefly.component.CompId
import com.inari.firefly.component.Component
import com.inari.firefly.component.ComponentType
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.property.VirtualPropertyRef
import com.inari.firefly.external.FFTimer
import com.inari.firefly.graphics.view.camera.CameraPivot
import com.inari.util.*
import com.inari.util.aspect.AspectType
import com.inari.util.indexed.Indexed

const val SYSTEM_FONT = "SYSTEM_FONT"

object GLBlendMode {
    const val GL_ZERO = 0x0
    const val GL_ONE = 0x1
    const val GL_SRC_COLOR = 0x300
    const val GL_ONE_MINUS_SRC_COLOR = 0x301
    const val GL_SRC_ALPHA = 0x302
    const val GL_ONE_MINUS_SRC_ALPHA = 0x303
    const val GL_DST_ALPHA = 0x304
    const val GL_ONE_MINUS_DST_ALPHA = 0x305
    const val GL_DST_COLOR = 0x306
    const val GL_ONE_MINUS_DST_COLOR = 0x307
    const val GL_SRC_ALPHA_SATURATE = 0x308
    const val GL_CONSTANT_COLOR = 0x8001
    const val GL_ONE_MINUS_CONSTANT_COLOR = 0x8002
    const val GL_CONSTANT_ALPHA = 0x8003
    const val GL_ONE_MINUS_CONSTANT_ALPHA = 0x8004
}

const val NO_NAME: String = "[[NO_NAME]]"
const val NO_STATE: String = "[[NO_STATE]]"
const val NO_PROGRAM: String = "[[NO_PROGRAM]]"
const val BASE_VIEW: String = "[[BASE_VIEW]]"

@JvmField val NO_NAMED = object : Named { override val name = NO_NAME }
@JvmField val NO_COMP_ID: CompId = CompId(-1, object : ComponentType<Component> {
    override val aspectIndex: Int get() = throw IllegalAccessException()
    override val aspectName: String get() = throw IllegalAccessException()
    override val aspectType: AspectType get() = throw IllegalAccessException()
    override val typeClass: Class<out Component> get() = throw IllegalAccessException()
})
@JvmField val NO_INDEXED = object : Indexed {
    override val index: Int = -1
    override val indexedTypeName: String = NO_NAME
}
@JvmField val NO_PROPERTY_REF: VirtualPropertyRef = object : VirtualPropertyRef {
    override val propertyName: String = NO_NAME
    override val type: Class<*> get() = throw IllegalAccessException()
    override fun accessor(entity: Entity): VirtualPropertyRef.PropertyAccessor = throw IllegalAccessException()
}
@JvmField val NO_CAMERA_PIVOT = object : CameraPivot {
    override fun init() = throw IllegalAccessException()
    override operator fun invoke(): PositionF = throw IllegalAccessException()
}

@JvmField val VOID_INT_CONSUMER: IntConsumer = { _ -> }
@JvmField val INT_FUNCTION_IDENTITY: IntFunction = { i -> i }
@JvmField val INT_FUNCTION_NULL: IntFunction = { _ -> 0 }
@JvmField val NULL_INT_FUNCTION: IntFunction = { _ -> throw IllegalStateException("NULL_INT_FUNCTION") }
@JvmField val NULL_INT_CONSUMER: IntConsumer = { _ -> throw IllegalStateException("NULL_INT_CONSUMER") }
@JvmField val EMPTY_INT_CONSUMER: IntConsumer = { _ -> }
@JvmField val NULL_CONSUMER: Consumer<Any> = { _ -> throw IllegalStateException("NULL_CONSUMER") }
@JvmField val NULL_CALL: Call = { throw IllegalStateException("NULL_CALL called") }
@JvmField val VOID_CALL: Call = {}
@JvmField val FALSE_SUPPLIER: BooleanSupplier = { false }
@JvmField val TRUE_SUPPLIER: BooleanSupplier = { true }
@JvmField val FALSE_PREDICATE: Predicate<Any> = { false }
@JvmField val TRUE_PREDICATE: Predicate<Any> = { true }

        @JvmField val INFINITE_SCHEDULER: FFTimer.Scheduler = object : FFTimer.Scheduler {
    override fun needsUpdate(): Boolean = true
}
@JvmField val EMPTY_INT_OPERATION: IntOperation = object : IntOperation {
    override operator fun invoke(index: Int): OpResult = OpResult.SUCCESS
}

@JvmField val TRUE_INT_PREDICATE: IntPredicate = object : IntPredicate {
    override operator fun invoke(index: Int): Boolean = true
}

@JvmField val FALSE_INT_PREDICATE: IntPredicate = object : IntPredicate {
    override operator fun invoke(index: Int): Boolean = false
}







