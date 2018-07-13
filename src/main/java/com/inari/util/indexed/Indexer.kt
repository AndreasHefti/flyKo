package com.inari.util.indexed

import java.util.*


interface Indexed {
    val index: Int
    val indexedTypeName: String
}

abstract class AbstractIndexed : Indexed {

    init { applyNewIndex() }

    abstract val indexer: Indexer

    @JvmField internal var iindex: Int = -1
    final override val index: Int
        get() = iindex
    final override val indexedTypeName
        get() = indexer.name

    protected fun disposeIndex() =
        indexer.disposeIndex(this)
    protected fun applyNewIndex() =
        indexer.applyNewIndex(this)
    protected fun finalize() =
        disposeIndex()
}

class Indexer(
    val name: String,
    val override: Boolean = false
) {

    private val indices: BitSet = BitSet()

    init {
        if (name in Indexer.indexer && !override)
            throw IllegalArgumentException("Indexer with name: $name already exists")

        indexer[name] = this
    }

    fun applyNewIndex(indexedSupplier: () -> AbstractIndexed) =
        applyNewIndex(indexedSupplier())

    fun applyNewIndex(indexed: AbstractIndexed) {
        typeCheck(indexed)

        if (indexed.index >= 0)
            disposeIndex(indexed)

        indexed.iindex = indices.nextClearBit(0)
        indices.set(indexed.iindex)
    }

    fun disposeIndex(indexedSupplier: () -> AbstractIndexed) =
        disposeIndex(indexedSupplier())

    fun disposeIndex(indexed: AbstractIndexed) {
        typeCheck(indexed)

        if (indexed.index >= 0)
            indices.clear(indexed.index)

        indexed.iindex = -1
    }

    fun dump(): String {
        val builder = StringBuilder()
        builder.append("Indexer : {\n")
        for ((key, value) in indexer) {
            builder.append("\n  ").append(key).append(" : ").append(value)
        }
        return builder.toString()
    }

    private fun typeCheck(indexed: AbstractIndexed) {
        if (indexed.indexer != this)
            throw IllegalArgumentException("Indexer with name: $name deny indexing of type: ${indexed.indexer.name}")
    }

    companion object {
        private val indexer: LinkedHashMap<String, Indexer> = LinkedHashMap()
    }
}