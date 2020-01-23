package com.inari.firefly

typealias Consumer<C> = (C) -> Unit
typealias Operation<C> = (C) -> OpResult
typealias Call = () -> Unit
typealias Supplier<C> = () -> C
typealias Receiver<C> = (C) -> C
typealias Predicate<C> = (C) -> Boolean

typealias IntFunction = (Int) -> Int
typealias IntOperation = (Int) -> OpResult
typealias IntSupplier = () -> Int
typealias IntConsumer = (Int) -> Unit
typealias IntPredicate = (Int) -> Boolean

typealias FloatFunction = (Float) -> Int
typealias FloatOperation = (Float) -> OpResult
typealias FloatSupplier = () -> Float
typealias FloatConsumer = (Float) -> Unit
typealias FloatPredicate = (Float) -> Boolean

typealias BooleanFunction = (Boolean) -> Boolean
typealias BooleanOperation = (Boolean) -> OpResult
typealias BooleanSupplier = () -> Boolean
typealias BooleanConsumer = (Boolean) -> Unit

interface Named {
    val name: String
}

enum class OpResult {
    SUCCESS,
    RUNNING,
    FAILED
}


