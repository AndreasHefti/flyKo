package com.inari.firefly

typealias Consumer<C> = (C) -> Unit

typealias Predicate<C> = (C) -> Boolean

typealias Call = () -> Unit

typealias Supplier<C> = () -> C

typealias Receiver<C> = (C) -> C

/** An Expression expecting an int. This is used instead of Consumer<Init> that is
 *  in fact a function (Int) -> Init to avoid auto boxing
 */
interface IntExpr {
    operator fun invoke(i: Int)
}

interface IntPredicate {
    operator fun invoke(i: Int): Boolean
}

interface IntSupplier {
    operator fun invoke(): Int
}

interface IntFunction {
    operator fun invoke(i: Int): Int
    companion object {
        fun identity() = object : IntFunction {
            override fun invoke(i: Int) = i
        }
    }
}

interface Condition {
    operator fun invoke(): Boolean
    companion object {
        fun trueCondition() = object : Condition {
            override fun invoke() = true
        }
        fun falseCondition() = object : Condition {
            override fun invoke() = false
        }
    }
}

interface IntConsumer {
    operator fun invoke(i: Int)
}

interface FloatConsumer {
    operator fun invoke(i: Float)
}

interface Named {
    val name: String
}


