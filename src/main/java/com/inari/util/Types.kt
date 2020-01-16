package com.inari.util

import java.util.*

/** Use this on types that can be disposed  */
interface Disposable {

    /** Dispose the instance  */
    fun dispose()

}

interface Loadable {
    fun load(): Disposable
}

/** Use this for types that can be cleared or are used in an abstract service that used to
 * clear object(s) but do not have to know the exact subType of the object(s)
 */
interface Clearable {

    /** Clears the instance  */
    fun clear()
}

interface IntIterator {
    operator fun hasNext(): Boolean
    operator fun next(): Int
}