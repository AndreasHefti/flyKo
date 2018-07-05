package com.inari.util.aspect

import com.inari.commons.lang.list.IntBag
import com.inari.util.indexed.Indexed
import java.util.*


interface Aspect : Indexed {
    val aspectGroup: AspectGroup
    val name: String
}

interface Aspected {
    val aspect: Aspects
}

class Aspects internal constructor(val group: AspectGroup, initSize: Int) : Iterable<Aspect> {

    internal val bitSet: BitSet = BitSet(initSize)
    private val tempBitSet: BitSet = BitSet(initSize)

    private constructor(source: Aspects) : this(source.group, source.size) {
        bitSet.or(source.bitSet)
    }

    val copy: Aspects
        get() = Aspects(this)

    val values: IntBag
        get() {
            val result = IntBag(bitSet.cardinality())
            var i = bitSet.nextSetBit(0)
            while (i >= 0) {
                result.add(i)
                i = bitSet.nextSetBit(i + 1)
            }
            return result
        }

    val valid: Boolean get() = !bitSet.isEmpty
    val size: Int get() = bitSet.size()
    val isEmpty: Boolean get() = bitSet.cardinality() <= 0

    operator fun plus(aspect: Aspect): Aspects {
        checkType(aspect)
        bitSet.set(aspect.index)
        return this
    }

    operator fun minus(aspect: Aspect): Aspects {
        checkType(aspect)
        bitSet.set(aspect.index, false)
        return this
    }

    operator fun plus(aspects: Aspects): Aspects {
        checkType(aspects)
        clear()
        bitSet.or(aspects.bitSet)
        return this
    }

    operator fun minus(aspects: Aspects): Aspects {
        checkType(aspects)
        bitSet.andNot(aspects.bitSet)
        return this
    }

    fun include(aspects: Aspects): Boolean {
        checkType(aspects)
        if (bitSet.isEmpty || aspects.bitSet.isEmpty)
            return false
        if (this == aspects)
            return true

        tempBitSet.clear()
        tempBitSet.or(bitSet)
        tempBitSet.and(aspects.bitSet)
        return tempBitSet == aspects.bitSet
    }

    fun exclude(aspects: Aspects): Boolean {
        checkType(aspects)
        if (bitSet.isEmpty || aspects.bitSet.isEmpty)
            return true
        if (this == aspects)
            return false

        tempBitSet.clear()
        tempBitSet.or(bitSet)
        tempBitSet.and(aspects.bitSet)
        return tempBitSet.isEmpty
    }

    fun intersects(aspects: Aspects): Boolean {
        checkType(aspects)
        return !exclude(aspects)
    }

    operator fun contains(aspect: Aspect): Boolean {
        checkType(aspect)
        return bitSet.get(aspect.index)
    }

    override fun iterator(): Iterator<Aspect> {
        return AspectIterator()
    }

    fun clear() {
        bitSet.clear()
        tempBitSet.clear()
    }

    private fun checkType(aspects: Aspects) {
        if (aspects.group !== group)
            throw IllegalArgumentException("Aspect aspectGroup missmatch: " + aspects.group + " " + group)
    }

    private fun checkType(aspect: Aspect) {
        if (aspect.aspectGroup !== group)
            throw IllegalArgumentException("Aspect subType missmatch: " + aspect.aspectGroup + " " + group)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("Aspects [aspectGroup=")
        builder.append(group)
        builder.append(" {")
        val iterator = AspectIterator()
        while (iterator.hasNext()) {
            builder.append(iterator.next().name)
            if (iterator.hasNext()) {
                builder.append(", ")
            }
        }
        builder.append("}")
        builder.append("]")
        return builder.toString()
    }

    private inner class AspectIterator : Iterator<Aspect> {

        private var nextSetBit = bitSet.nextSetBit(0)

        override fun hasNext(): Boolean = nextSetBit >= 0

        override fun next(): Aspect {
            val aspect = group.getAspect(nextSetBit)
            nextSetBit = bitSet.nextSetBit(nextSetBit + 1)
            return aspect
        }
    }

}