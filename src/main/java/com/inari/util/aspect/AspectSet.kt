package com.inari.util.aspect

import com.inari.util.collection.DynArray
import com.inari.util.indexed.Indexed

class AspectSet<T> constructor(
    val aspectType: AspectType,
    val baseType: Class<T>
) : AspectAware, Iterable<Aspect> {

    private val values: DynArray<T> = DynArray.Companion.of(baseType, 10, 5)

    override var aspects: Aspects = aspectType.createAspects()
        private set

    var size: Int = 0
        private set

    fun include(aspects: Aspects): Boolean =
        this.aspects.include(aspects)
    fun exclude(aspects: Aspects): Boolean =
        this.aspects.exclude(aspects)
    val length: Int get() =
        aspects.size

    fun set(aspect: Aspect, value: T) {
        if (aspectType.typeCheck(aspect)) {
            aspects + aspect
            values[aspect.aspectIndex] = value
        } else {
            throw AspectTypeMismatchException("this-aspect-type: ${this.aspectType} other-aspect-type: ${aspect.aspectType}")
        }
    }

    fun get(aspect: Aspect): T? {
        if (aspectType.typeCheck(aspect))
            return values[aspect.aspectIndex]
        return null
    }


    fun <TT : T> get(aspect: Aspect, subType: Class<TT>): TT? =
        get(aspect)?.run { subType.cast(this) }

    operator fun plus(other: AspectSet<T>) {
        if (other.aspectType == this.aspectType) {
            for (aspect in other.aspects)
                this.set(aspect, other.values[aspect.aspectIndex]!!)
        }
    }

    operator fun contains(aspect: Aspect): Boolean =
        aspects.contains(aspect)

    fun clear() {
        aspects.clear()
        values.clear()
    }

    override fun iterator(): Iterator<Aspect> = aspects.iterator()
    fun getIterable(): Iterable<Aspect> = aspects

}