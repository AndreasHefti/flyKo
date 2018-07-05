package com.inari.util.indexed

import com.inari.util.aspect.AspectGroup


class TestIndexedObject : BaseIndexedObject() {
     override val indexedType get() = TestIndexedObject::class.java
}

abstract class A protected constructor(override val indexedTypeKey: IIndexedTypeKey) : IndexedType {
    class AIndexedTypeKey(subType: Class<out A>) : IndexedTypeKey(A::class.java, subType, ASPECT_GROUP) {
        companion object {
            private val ASPECT_GROUP = AspectGroup("AIndexedTypeKey")
        }
    }
}

class AA : A(Indexer.getOrCreateIndexedTypeKey(AIndexedTypeKey::class.java, AA::class.java) { AIndexedTypeKey(AA::class.java) } )
class AB : A(Indexer.getOrCreateIndexedTypeKey(AIndexedTypeKey::class.java, AB::class.java) { AIndexedTypeKey(AB::class.java) } )
class AC : A(Indexer.getOrCreateIndexedTypeKey(AIndexedTypeKey::class.java, AC::class.java) { AIndexedTypeKey(AC::class.java) } )

interface B : IndexedType {
    class BIndexedTypeKey constructor(indexedType: Class<out B>) : IndexedTypeKey(B::class.java, indexedType, ASPECT_GROUP) {
        companion object {
            private val ASPECT_GROUP = AspectGroup("BIndexedTypeKey")
        }
    }
}

class BA : B {
    init {Indexer.getIndexedTypeKey(B.BIndexedTypeKey::class.java, BA::class.java)}
    override val indexedTypeKey = TYPE_KEY
    companion object {
        val TYPE_KEY = Indexer.getOrCreateIndexedTypeKey(
            B.BIndexedTypeKey::class.java,
            BA::class.java
        ) { B.BIndexedTypeKey(BA::class.java) }
    }
}

class BB : B {
    init {Indexer.getIndexedTypeKey(B.BIndexedTypeKey::class.java, BB::class.java)}
    override val indexedTypeKey = TYPE_KEY
    companion object {
        val TYPE_KEY = Indexer.getOrCreateIndexedTypeKey(
            B.BIndexedTypeKey::class.java,
            BB::class.java
        ) { B.BIndexedTypeKey(BB::class.java) }
    }
}

class BC: B {
    init {Indexer.getIndexedTypeKey(B.BIndexedTypeKey::class.java, BC::class.java)}
    override val indexedTypeKey = TYPE_KEY
    companion object {
        val TYPE_KEY = Indexer.getOrCreateIndexedTypeKey(
            B.BIndexedTypeKey::class.java,
            BC::class.java
        ) { B.BIndexedTypeKey(BC::class.java) }
    }
}

