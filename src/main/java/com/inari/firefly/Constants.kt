package com.inari.firefly

import com.inari.commons.geom.BitMask
import com.inari.commons.lang.aspect.AspectGroup
import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.firefly.component.CompId
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.property.IVirtualPropertyRef
import com.inari.firefly.external.FFTimer
import com.inari.firefly.physics.contact.Contact


@JvmField val NO_NAME: String = "[[NO_NAME]]"
@JvmField val NO_STATE: String = "[[NO_STATE]]"
@JvmField val NO_PROGRAM: String = "[[NO_PROGRAM]]"
@JvmField val NO_COMP_ID: CompId = CompId(-1, object : IIndexedTypeKey {
    override fun index(): Int = throw IllegalAccessException()
    override fun baseType(): Class<*> = throw IllegalAccessException()
    override fun <T> type(): Class<T> = throw IllegalAccessException()
    override fun aspectGroup(): AspectGroup = throw IllegalAccessException()
    override fun name(): String = NO_NAME
})
@JvmField val NULL_CONTACT = Contact()

@JvmField val INFINITE_SCHEDULER: FFTimer.Scheduler = object : FFTimer.Scheduler {
    override fun needsUpdate(): Boolean = true
}

@JvmField val NO_PROPERTY_REF: IVirtualPropertyRef = object : IVirtualPropertyRef {
    override val propertyName: String = NO_NAME
    override val type: Class<*>
        get() = throw IllegalAccessException()
    override fun accessor(entity: Entity): IVirtualPropertyRef.PropertyAccessor =
        throw IllegalAccessException()
}

@JvmField val BASE_VIEW: String = "[[BASE_VIEW]]"

fun <T> VOID_EXPR(): Expr<T> = { throw IllegalStateException("VOID_EXPR called") }

@JvmField val VOID_INT_EXPR: IntExpr = object : IntExpr {
    override fun invoke(i: Int) { throw IllegalStateException("VOID_INT_EXPR called") }
}

@JvmField val VOID_CALL: Call = { throw IllegalStateException("VOID_CALL called") }

@JvmField val FALSE_CONDITION: Condition = object : Condition {
    override fun invoke(): Boolean = false
}

@JvmField val TRUE_CONDITION: Condition = object : Condition {
    override fun invoke(): Boolean = true
}

fun <T> TRUE_PREDICATE(): Predicate<T> = {true}
fun <T> FALSE_PREDICATE(): Predicate<T> = {false}



object GLBlendMode {
    val GL_ZERO = 0x0
    val GL_ONE = 0x1
    val GL_SRC_COLOR = 0x300
    val GL_ONE_MINUS_SRC_COLOR = 0x301
    val GL_SRC_ALPHA = 0x302
    val GL_ONE_MINUS_SRC_ALPHA = 0x303
    val GL_DST_ALPHA = 0x304
    val GL_ONE_MINUS_DST_ALPHA = 0x305
    val GL_DST_COLOR = 0x306
    val GL_ONE_MINUS_DST_COLOR = 0x307
    val GL_SRC_ALPHA_SATURATE = 0x308
    val GL_CONSTANT_COLOR = 0x8001
    val GL_ONE_MINUS_CONSTANT_COLOR = 0x8002
    val GL_CONSTANT_ALPHA = 0x8003
    val GL_ONE_MINUS_CONSTANT_ALPHA = 0x8004
}

