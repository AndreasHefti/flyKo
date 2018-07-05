package com.inari.util.aspect

import com.inari.commons.lang.list.DynArray

import java.util.HashSet

class AspectGroup(private val name: String) {

    private val aspects = DynArray.create(Aspect::class.java, 10, 10)
    private val hashCode: Int

    init {
        if (NAMES.contains(name))
            throw IllegalArgumentException()

        NAMES.add(name)

        val prime = 31
        var result = 1
        result = prime * result + name.hashCode()
        hashCode = result
    }

    fun createAspects(): Aspects =
        Aspects(this, aspects.size())

    fun createAspects(vararg aspects: Aspect): Aspects {
        val result = Aspects(this, this.aspects.size())
        for (aspect in aspects) {
            result + aspect
        }
        return result
    }

    fun getAspect(index: Int): Aspect = aspects.get(index)

    fun getAspect(name: String): Aspect? {
        for (aspect in aspects)
            if (name == aspect.name)
                return aspect
        return null
    }

    fun createAspect(name: String): Aspect {
        if (getAspect(name) != null)
            throw IllegalArgumentException("An Aspect with name: " + name + " already exists for this aspect aspectGroup: " + this)

        val result = AspectImpl(this, aspects.size(), name)
        aspects.add(result)
        return result
    }

    fun createAspect(name: String, index: Int): Aspect {
        if (aspects.contains(index))
            throw IllegalArgumentException("An Aspect with index: " + index + " already exists for this aspect aspectGroup: " + this)
        if (getAspect(name) != null)
            throw IllegalArgumentException("An Aspect with name: " + name + " already exists for this aspect aspectGroup: " + this)

        val result = AspectImpl(this, index, name)
        aspects.set(index, result)
        return result
    }

    fun disposeAspect(index: Int) = aspects.remove(index)

    override fun hashCode(): Int = hashCode

    override fun toString(): String = name

    @Throws(Throwable::class)
    fun finalize() = NAMES.remove(name)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AspectGroup

        if (name != other.name) return false
        if (aspects != other.aspects) return false
        if (hashCode != other.hashCode) return false

        return true
    }


    private inner class AspectImpl(
        override val aspectGroup: AspectGroup,
        override val index: Int,
        override val name: String
    ) : Aspect

    companion object {
        private val NAMES = HashSet<String>()
    }

}
