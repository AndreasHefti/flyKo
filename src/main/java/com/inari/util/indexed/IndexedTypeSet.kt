package com.inari.util.indexed

import com.inari.util.Clearable
import com.inari.util.aspect.Aspect
import com.inari.util.aspect.AspectGroup
import com.inari.util.aspect.Aspects

class IndexedTypeSet constructor(
    val indexedBaseType: Class<out IndexedTypeKey>,
    aspectGroup: AspectGroup,
    length: Int = 10
) {

    var aspect: Aspects = aspectGroup.createAspects()
        private set
    private var array: Array<IndexedType?> = arrayOfNulls(length)
    var size: Int = 0
        private set

    fun include(aspects: Aspects): Boolean = aspect.include(aspects)
    fun exclude(aspects: Aspects): Boolean = aspect.exclude(aspects)
    val length: Int get() = aspect.size


    operator fun plus(indexedType: IndexedType): IndexedType? {
        val typeIndex = indexedType.indexedTypeKey.index
        ensureCapacity(typeIndex)

        val old = array[typeIndex]
        array[typeIndex] = indexedType
        aspect + indexedType.indexedTypeKey
        if (old == null)
            size++

        return old
    }

    operator fun plus(other: IndexedTypeSet) {
        ensureCapacity(other.length)
        for (i in 0 until other.length) {
            val value = other.array[i]
            if (value != null)
                this + value
        }
    }

    operator fun contains(aspect: Aspect): Boolean =
        contains(aspect.index)
    operator fun contains(index: Int): Boolean =
        if (index < 0 || index >= array.size) false
        else array[index] != null


    operator fun minus(indexedType: IndexedType): IndexedType? =
        this.minus(indexedType.indexedTypeKey.index)

    operator fun minus(indexedTypeKey: IndexedTypeKey): IndexedType? =
        minus(indexedTypeKey.index)

    operator fun minus(index: Int): IndexedType? {
        val result = array[index]
        array[index] = null
        if (result != null) {
            size--
            aspect - result.indexedTypeKey
        }
        return result
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <I : IndexedType> get(indexedTypeKey: IndexedTypeKey): I? =
        array[indexedTypeKey.index] as I

    operator fun <I : IndexedType> get(type: Class<I>): I? {
        val typeIndex = Indexer.getIndexedTypeKey(indexedBaseType, type)!!.index
        return type.cast(array[typeIndex])
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <I : IndexedType> get(typeIndex: Int): I =
        this.array[typeIndex] as I

    fun <T : IndexedType> getIterable(): Iterable<T> {
        return object : Iterable<T> {
            override fun iterator(): Iterator<T> {
                return IndexedIterator()
            }
        }
    }

    private fun ensureCapacity(index: Int) {
        if (index < array.size)
            return

        val newArray = arrayOfNulls<IndexedType>(index + 1)
        System.arraycopy(array, 0, newArray, 0, array.size)
        array = newArray
    }

    fun clear() {
        aspect.clear()
        size = 0
        var i = 0
        while (i < array.size) {
            if (array[i] != null && array[i] is Clearable)
                (array[i] as Clearable).clear()
            array[i] = null
            i++
        }
    }

    override fun toString(): String {
        val sb = StringBuffer()
        sb.append("IndexedTypeSet [ indexedBaseType=").append(indexedBaseType.simpleName)
        sb.append(" length=").append(array.size).append(" size:").append(size)
        sb.append(", indexed={")
        var deleteLastSep = false
        for (i in array.indices) {
            deleteLastSep = true
            sb.append(if (array[i] != null) array[i]!!::class.java.simpleName else "null" ).append(",")
        }
        if (deleteLastSep) {
            sb.deleteCharAt(sb.length - 1)
        }
        sb.append("} ]")
        return sb.toString()
    }

    private inner class IndexedIterator<T : IndexedType> : Iterator<T> {
        private val delegate = aspect.iterator()
        override fun hasNext(): Boolean = delegate.hasNext()
        @Suppress("UNCHECKED_CAST")
        override fun next(): T = array[delegate.next().index] as T
    }

}
