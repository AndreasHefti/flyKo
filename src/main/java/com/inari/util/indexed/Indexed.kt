package com.inari.util.indexed

import com.inari.util.Disposable
import com.inari.util.aspect.Aspect
import com.inari.util.aspect.AspectGroup

interface Indexed {
    val index: Int
}

/** Defines the interface for a subType that can be indexed by sub-subType.
 *
 * If there should be a base subType for that base subType all sub types should have a unique index id
 * this can be implemented by the base subType.
 *
 */
interface IndexedType {

    /** Gets the IIndexedTypeKey that defines the key and base subType of the IndexedType  */
    val indexedTypeKey: IIndexedTypeKey

}

interface IndexedObject : Indexed {
    val indexedType: Class<out IndexedObject>
}

/** Defines the key for an IndexedType with the index value for specified subType or indexed subType.
 *
 * // TODO describe or refer to an example
 *
 */
interface IIndexedTypeKey : IndexedObject, Aspect {
    override val indexedType: Class<out IIndexedTypeKey>
    val subType: Class<*>
    val baseType: Class<*>
}

abstract class BaseIndexedObject : IndexedObject, Disposable {

    protected var iindex = -1

    protected constructor(indexedId: Int = -1) {
        setIndex(indexedId)
        if (indexedId >= 0) {
            Indexer.setIndexedObjectIndex(this)
        }
    }

    protected constructor(skipAutoInit: Boolean) {
        if (skipAutoInit) iindex = -1
        else setIndex(-1)
    }

    final override val index: Int
        get() = iindex

    protected fun setIndex(indexedId: Int) {
        if (iindex >= 0)
            Indexer.disposeIndexedObject(this)

        iindex = if (indexedId < 0) Indexer.nextObjectIndex(indexedType)
        else indexedId
    }

    override fun dispose() {
        if (index < 0)
            return
        Indexer.disposeIndexedObject(this)
        iindex = -1
    }

    @Throws(Throwable::class)
    protected fun finalize() = dispose()

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + index
        result = prime * result + indexedType.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null)
            return false
        if (javaClass != other.javaClass)
            return false
        other as BaseIndexedObject?
        if (index != other.index)
            return false
        if (indexedType != other.indexedType)
            return false
        return true
    }
}

abstract class IndexedTypeKey internal constructor(
    final override val baseType: Class<out IndexedType>,
    final override val subType: Class<out IndexedType>,
    final override val aspectGroup: AspectGroup
) : BaseIndexedObject(), IIndexedTypeKey {

    final override val indexedType: Class<out IIndexedTypeKey>
        get() = this::class.java
    final override val name: String
        get() = subType.name.replace(".", "_")

    init {
        aspectGroup.createAspect(name, index)
    }

    override fun toString(): String =
        "[${indexedType.simpleName}:${baseType.simpleName}:${subType.simpleName}:$index"


    internal fun disable() {
        aspectGroup.disposeAspect(index)
        iindex = -1
    }

    internal fun enable(index: Int) {
        iindex = index
        aspectGroup.createAspect(name, super.index)
    }
}

//-----------------------------------------------
interface IIndexedObject {
    val objectIndex: Int
    val indexedType: Class<*>
}
interface IIndexedType {
    val typeIndex: Int
    val indexed: IIndexedObject
    val baseType: Class<*>
    val subType: Class<*>
}

