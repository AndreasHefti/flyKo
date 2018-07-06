package com.inari.util.indexed

import com.inari.util.contains
import java.util.*

object IndexProvider {

    private val indexedObjectTypes = LinkedHashMap<Class<out IIndexedObject>, BitSet>()
    private val indexedTypeKeys = LinkedHashMap<Class<out IIndexedObject>, LinkedHashSet<NewIndexedTypeKey>>()

    /** Within nextObjectIndex one can get the next index for a specified subType of IndexedObject.
     * @param indexedType The specified subType of IndexedObject
     * @return the next index for a specified subType of IndexedObject.
     */
    internal fun nextObjectIndex(indexedType: Class<out IIndexedObject>): Int =
        findFreeObjectIndex(
            indexedObjectTypes.getOrPut(indexedType) { BitSet() }
        )

    /** The the number of indices that are used for a specified IndexedObject subType.
     * @param indexedType The specific subType of IndexedObject
     * @return the number of indices that are used for a specified IndexedObject subType.
     */
    fun numOfIndexedObjectTypes(indexedType: Class<out IIndexedObject>): Int {
        val indexSet = indexedObjectTypes[indexedType] ?: return 0
        var lastIndex = 0
        var i = indexSet.nextSetBit(0)
        while (i >= 0) {
            lastIndex = i
            i = indexSet.nextSetBit(i + 1)
        }
        return lastIndex + 1
    }

    /** This is used by BaseIndexedObject to release a specified index when the BaseIndexedObject instance is
     * disposed and not used anymore.
     * @param indexedObject the IndexedObject instance to dispose
     */
    internal fun disposeObjectIndex(indexedObject: IIndexedObject) =
        disposeObjectIndex(indexedObject.indexedType, indexedObject.objectIndex)

    internal fun disposeObjectIndex(indexedType: Class<out IIndexedObject>, objectIndex: Int) {
        val indexSet = indexedObjectTypes[indexedType]
        indexSet?.clear(objectIndex)
    }

    /** This is used by BaseIndexedObject on new instance that has already a index assigned to
     * register this index to the Indexer.
     * @param indexedObject the IndexedObject instance form that the index is get to register
     * @throws IllegalArgumentException if the index from specified indexedObject is already used by another living IndexedObject instance
     */
    internal fun setIndexedObjectIndex(indexedObject: IIndexedObject) =
        registerIndex(indexedObject.indexedType, indexedObject.objectIndex)

    internal fun registerIndex(indexedObjectType: Class<out IIndexedObject>, index: Int): Int {
        val indexSet: BitSet = indexedObjectTypes.getOrPut(indexedObjectType) { BitSet() }
        if (index in indexSet)
            throw IllegalArgumentException("The Object: " + indexedObjectType.name + " index: " + index + " is already used by another Object!")
        indexSet.set(index)
        return index
    }

    internal fun register(indexedTypeKey: NewIndexedTypeKey) {
        val keys: LinkedHashSet<NewIndexedTypeKey> = indexedTypeKeys.getOrPut(indexedTypeKey.indexedType) {
            LinkedHashSet()
        }
        keys.add(indexedTypeKey)
    }

    internal fun dispose(indexedTypeKey: NewIndexedTypeKey) {
        val keys: LinkedHashSet<NewIndexedTypeKey> = indexedTypeKeys.getOrPut(indexedTypeKey.indexedType) {
            LinkedHashSet()
        }
        keys.remove(indexedTypeKey)
    }

    internal fun clear() {

        for ((_, value) in indexedTypeKeys) {
            for (indexedTypeKey in LinkedHashSet(value)) {
                dispose(indexedTypeKey)
            }
        }
        indexedTypeKeys.clear()
        indexedObjectTypes.clear()

    }


    /** Finds the next free index on the specified BitSet.
     * This is used to reuse once used but already disposed indexes
     * @param indexSet the BitSet of a specified indexedObject subType
     * @return the next free index.
     */
    private fun findFreeObjectIndex(indexSet: BitSet): Int {
        val nextClearBit = indexSet.nextClearBit(0)
        indexSet.set(nextClearBit)
        return nextClearBit
    }

    fun dump(): String {
        val builder = StringBuilder()
        builder.append("IndexProvider : {\n")
        builder.append(" * Indexed Objects :")
        for ((key, value) in indexedObjectTypes) {
            builder.append("\n  ").append(key.simpleName).append(" : ").append(value)
        }

        builder.append("\n * Indexed Type Keys :")
        for ((_, value) in indexedTypeKeys) {
            for (indexedTypeKey in value) {
                builder.append("\n    ").append(indexedTypeKey)
            }
        }
        builder.append("\n}")
        return builder.toString()
    }

    override fun toString(): String {
        return "IndexProvider(indexedObjectTypes=$indexedObjectTypes)"
    }

}