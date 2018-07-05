/*******************************************************************************
 * Copyright (c) 2015 - 2016 - 2016, Andreas Hefti, inarisoft@yahoo.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inari.util.indexed

import com.inari.util.contains
import java.util.*

/** Indexer is used to indexing either IndexedObject or IndexedType.
 *
 * The concept of IndexedObject is the possibility to indexing object instanced of a certain subType.
 * For example there is a class A and for every instance of the class A there should be an index,
 * the class A can either extends the BaseIndexedObject to get all its benefits to get the indexed
 * automatically or it can implements IndexedObject and implements the index creation by it self using
 * the Indexer like BaseIndexedObject does.
 *
 * The concept of IndexedType is to index all different sub-types of a certain IndexedBaseType so that
 * the index of a IndexedType instance identifies the sub-subType of IndexedBaseType by an index.
 * For example if there is a class (subType) A for that all sub-types has a specified index to identify,
 * A has to implements IndexedBaseType and returns its owen subType (A.class) for indexedBaseType.
 * Normally A is an interface or abstract class.
 * Then for every sub-subType of A, for example B extends A and C extends A, the class has to implement
 * IndexedType and returns its own subType for indexedType (B.class and C.class). Within the Indexer
 * one is able to generate and use an index for the specified subType B and C and is able to identify the
 * subType by index.
 *
 * For more example see also the Test cases of Indexer.
 */
object Indexer {

    private val indexedTypeKeys = LinkedHashSet<IndexedTypeKey>()
    private val indexedObjectTypes = LinkedHashMap<Class<out IndexedObject>, BitSet>()


    // **** OBJECT INDEX *********************************************************

    /** Within nextObjectIndex one can get the next index for a specified subType of IndexedObject.
     * @param indexedType The specified subType of IndexedObject
     * @return the next index for a specified subType of IndexedObject.
     */
    fun nextObjectIndex(indexedType: Class<out IndexedObject>): Int =
        findFreeObjectIndex(
            indexedObjectTypes.getOrPut(indexedType) { BitSet() }
        )

    /** The the number of indices that are used for a specified IndexedObject subType.
     * @param indexedType The specific subType of IndexedObject
     * @return the number of indices that are used for a specified IndexedObject subType.
     */
    fun getIndexedObjectSize(indexedType: Class<out IndexedObject>): Int {
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
    internal fun disposeIndexedObject(indexedObject: IndexedObject) {
        val indexSet = indexedObjectTypes[indexedObject.indexedType]
        indexSet?.clear(indexedObject.index)
    }

    /** Use this to dispose or free a given index for a specified object instance of specified subType.
     * @param indexedObjectType the indexed object subType
     * @param index the index to dispose/free
     */
    fun disposeObjectIndex(indexedType: Class<out IndexedObject>, index: Int) {
        val indexSet = indexedObjectTypes[indexedType]
        indexSet?.clear(index)
    }

    /** This is used by BaseIndexedObject on new instance that has already a index assigned to
     * register this index to the Indexer.
     * @param indexedObject the IndexedObject instance form that the index is get to register
     * @throws IllegalArgumentException if the index from specified indexedObject is already used by another living IndexedObject instance
     */
    internal fun setIndexedObjectIndex(indexedObject: IndexedObject) =
        registerIndex(indexedObject.indexedType, indexedObject.index)

    fun registerIndex(indexedObjectType: Class<out IndexedObject>, index: Int) {
        val indexSet: BitSet = indexedObjectTypes.getOrPut(indexedObjectType) { BitSet() }
        if (index in indexSet)
            throw IllegalArgumentException("The Object: " + indexedObjectType.name + " index: " + index + " is already used by another Object!")
        indexSet.set(index)
    }

    fun <K: IndexedTypeKey> getOrCreateIndexedTypeKey(
        indexedType: Class<K>,
        subType: Class<out IndexedType>,
        newKey: () -> K
    ): K {
        var key = getIndexedTypeKey(indexedType, subType)
        if (key != null) {
            return key
        }

        key = newKey()
        if (!key.baseType.isAssignableFrom(subType)) {
            key.disable()
            throw IllegalArgumentException("IndexedType mismatch: subType: $subType is not a valid substitute of base type: ${key.baseType}")
        }

        if (key.index < 0)
            key.enable(nextObjectIndex(key.indexedType))
        indexedTypeKeys.add(key)
        return key
    }

    fun <K: IndexedTypeKey> getIndexedTypeKey(indexedType: Class<K>, subType: Class<out IndexedType>): K? {
        for (indexedTypeKey in indexedTypeKeys) {
            if (subType == indexedTypeKey.subType && indexedType == indexedTypeKey.indexedType) {
                if (indexedTypeKey.index < 0) {
                    indexedTypeKey.enable(Indexer.nextObjectIndex(indexedTypeKey.indexedType))
                }
                @Suppress("UNCHECKED_CAST")
                return indexedTypeKey as K
            }
        }

        return null
    }

    internal fun removeIndexedTypeKey(key: IndexedTypeKey) {
        indexedTypeKeys.remove(key)
    }

    internal fun getTypeIndexForBaseType(indexedType: Class<out IndexedObject>): Int {
        for (indexedTypeKey in indexedTypeKeys) {
            if (indexedTypeKey.indexedType == indexedType) {
                return indexedTypeKey.index
            }
        }

        return -1
    }

    fun getIndexedTypeKeyForIndex(indexedType: Class<out IndexedObject>, index: Int): IIndexedTypeKey? {
        for (indexedTypeKey in indexedTypeKeys) {
            if (indexedType == indexedTypeKey.indexedType && index == indexedTypeKey.index) {
                return indexedTypeKey
            }
        }

        return null
    }

    fun clear() {
        indexedObjectTypes.clear()
        for (indexedTypeKey in indexedTypeKeys) {
            indexedTypeKey.disable()
        }
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

    /** Creates a dump String of Indexed within all registrations
     * @return a dump String of Indexed within all registrations
     */
    fun dump(): String {
        val builder = StringBuilder()
        builder.append("IndexProvider : {\n")
        builder.append(" * Indexed Objects :")
        for ((key, value) in indexedObjectTypes) {
            builder.append("\n  ").append(key.simpleName).append(" : ").append(value)
        }

        val grouped = LinkedHashMap<String, MutableSet<IndexedTypeKey>>()
        for (indexedTypeKey in indexedTypeKeys) {
            if (indexedTypeKey.index < 0) {
                continue
            }
            val baseType = indexedTypeKey.indexedType.simpleName
            grouped.getOrPut(baseType) {LinkedHashSet()}
                .add(indexedTypeKey)
        }
        builder.append("\n * Indexed Type Keys :")
        for ((key, value) in grouped) {
            builder.append("\n  ").append(key).append(":")
            for (indexedTypeKey in value) {
                builder.append("\n    ").append(indexedTypeKey.subType.simpleName).append(" : ").append(indexedTypeKey.index)
            }
        }
        builder.append("\n}")
        return builder.toString()
    }

}
