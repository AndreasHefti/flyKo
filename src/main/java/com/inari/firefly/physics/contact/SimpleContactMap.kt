package com.inari.firefly.physics.contact

import com.inari.commons.geom.Rectangle
import com.inari.commons.lang.IntIterator
import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.entity.Entity
import com.inari.firefly.system.component.SubType
import java.util.*

class SimpleContactMap : ContactMap() {

    private val entities = BitSet(100)
    private val pool = ArrayDeque<EntityIdIterator>()
    init {
        pool.add(EntityIdIterator())
        pool.add(EntityIdIterator())
    }

    override fun add(entity: Entity) =
        entities.set(entity.index())


    override fun remove(entity: Entity) =
        entities.set(entity.index(), false)


    override fun update(entity: Entity) {
        // not needed here
    }

    override fun get(region: Rectangle, entity: Entity): IntIterator {
        if (pool.isEmpty())
            pool.add(EntityIdIterator())

        val iterator = pool.pop()
        iterator.reset(entity.index())
        return iterator
    }

    override fun clear() =
        entities.clear()



    companion object : SubType<SimpleContactMap, ContactMap>() {
        override val typeKey: IndexedTypeKey = SimpleContactMap.typeKey
        override fun subType() = SimpleContactMap::class.java
        override fun createEmpty() = SimpleContactMap()
    }


    private inner class EntityIdIterator : IntIterator {

        private var index: Int = entities.nextSetBit(0)
        private var exclude: Int = -1

        override fun hasNext(): Boolean =
            index >= 0

        override fun next(): Int {
            val result = index
            findNext()
            return result
        }

        private fun findNext() {
            index = entities.nextSetBit(index + 1)
            if (index == exclude)
                index = entities.nextSetBit(index + 1)

            if (index < 0)
                pool.add(this)
        }

        internal fun reset(exclude: Int) {
            index = entities.nextSetBit(0)
            this.exclude = exclude
        }
    }
}