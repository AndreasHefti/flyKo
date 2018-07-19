package com.inari.util.aspect

import com.inari.util.Clearable
import com.inari.util.indexed.Indexed

class AspectTypeSet constructor(
    private val aspectType: AspectType,
    length: Int = 10
) : AspectAware {

    override var aspects: Aspects = aspectType.createAspects()
        private set
    private var array: Array<Indexed?> = arrayOfNulls(length)
    var size: Int = 0
        private set

    fun include(aspects: Aspects): Boolean =
        this.aspects.include(aspects)
    fun exclude(aspects: Aspects): Boolean =
        this.aspects.exclude(aspects)
    val length: Int get() =
        aspects.size


    operator fun plus(indexed: Indexed): Indexed? {
        val typeIndex = indexed.index
        ensureCapacity(typeIndex)

        val old = array[typeIndex]
        array[typeIndex] = indexed
        aspects + indexed
        if (old == null)
            size++

        return old
    }

    operator fun plus(other: AspectTypeSet) {
        ensureCapacity(other.length)
        for (i in 0 until other.length) {
            val value = other.array[i]
            if (value != null)
                this + value
        }
    }

    operator fun contains(aspect: Aspect): Boolean =
        contains(aspect.aspectIndex)
    operator fun contains(index: Int): Boolean =
        if (index < 0 || index >= array.size) false
        else array[index] != null

    operator fun minus(indexed: Indexed): Indexed? =
        this.minus(indexed.index)

    operator fun minus(index: Int): Indexed? {
        val result = array[index]
        array[index] = null
        if (result != null) {
            size--
            aspects - result
        }
        return result
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <I : Indexed> get(aspect: Aspect): I =
        array[aspect.aspectIndex] as I ?: throw IllegalArgumentException()

//    operator fun <I : IndexedType> get(type: Class<I>): I? {
//        val typeIndex = IndexProvider.getIndexedTypeKey(indexedBaseType, type).typeIndex
//        return type.cast(array[typeIndex])
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    operator fun <I : IndexedType> get(typeIndex: Int): I =
//        this.array[typeIndex] as I

    fun <A : Aspect> getIterable(): Iterable<A> {
        return object : Iterable<A> {
            override fun iterator(): Iterator<A> {
                return IndexedIterator()
            }
        }
    }

    private fun ensureCapacity(index: Int) {
        if (index < array.size)
            return

        val newArray = arrayOfNulls<Indexed>(index + 1)
        System.arraycopy(array, 0, newArray, 0, array.size)
        array = newArray
    }

    fun clear() {
        aspects.clear()
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
        sb.append("AspectTypeSet [ baseType=").append(aspectType.name)
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

    private inner class IndexedIterator<A : Aspect> : Iterator<A> {
        private val delegate = aspects.iterator()
        override fun hasNext(): Boolean = delegate.hasNext()
        @Suppress("UNCHECKED_CAST")
        override fun next(): A = array[delegate.next().aspectIndex] as A
    }

}
