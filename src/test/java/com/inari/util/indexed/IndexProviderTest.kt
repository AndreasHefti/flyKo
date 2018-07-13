package com.inari.util.indexed

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IndexTest {

    @Before
    fun init() {
        IndexProvider.clear()
    }

    @Test
    fun testSimpleIndexedObject() {
        assertEquals(
            "IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            " * Indexed Type Keys :\n" +
            "}",
            IndexProvider.dump())

        val o1 = SimpleIndexedObject()
        val o2 = SimpleIndexedObject()
        val o3 = SimpleIndexedObject()

        assertEquals("IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            "  SimpleIndexedObject : {0, 1, 2}\n" +
            " * Indexed Type Keys :\n" +
            "}", IndexProvider.dump())
        assertTrue(o1.index == 0)
        assertTrue(o2.index == 1)
        assertTrue(o3.index == 2)
        assertTrue(o1.indexedTypeName == "SimpleIndexedObject")

        o1.dispose()
        assertEquals("IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            "  SimpleIndexedObject : {1, 2}\n" +
            " * Indexed Type Keys :\n" +
            "}", IndexProvider.dump())
        assertTrue(o1.index == -1)

        val o4 = SimpleIndexedObject()
        assertTrue(o4.index == 0)

        o1.applyNew()
        assertTrue(o1.index == 3)
        o1.applyNew()
        assertTrue(o1.index == 3)
        assertEquals("IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            "  SimpleIndexedObject : {0, 1, 2, 3}\n" +
            " * Indexed Type Keys :\n" +
            "}", IndexProvider.dump())
    }

    @Test
    fun testSimpleIndexedType() {
        assertEquals("IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            " * Indexed Type Keys :\n" +
            "}", IndexProvider.dump())

        val a1 = SITA()
        assertEquals("IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            "  SimpleIndexedTypeKey : {0}\n" +
            " * Indexed Type Keys :\n" +
            "    TypeKey[SimpleIndexedTypeKey:SimpleIndexedType:SITA:0]\n" +
            "}", IndexProvider.dump())
        assertTrue(SITA.index == 0)
        assertTrue(SITA.indexedTypeName == "IndexedType")

        val a2 = SITA()
        assertEquals("IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            "  SimpleIndexedTypeKey : {0}\n" +
            " * Indexed Type Keys :\n" +
            "    TypeKey[SimpleIndexedTypeKey:SimpleIndexedType:SITA:0]\n" +
            "}", IndexProvider.dump())
        assertTrue(SITA.index == 0)


        val b1 = SITB()
        assertEquals("IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            "  SimpleIndexedTypeKey : {0, 1}\n" +
            " * Indexed Type Keys :\n" +
            "    TypeKey[SimpleIndexedTypeKey:SimpleIndexedType:SITA:0]\n" +
            "    TypeKey[SimpleIndexedTypeKey:SimpleIndexedType:SITB:1]\n" +
            "}", IndexProvider.dump())
        assertTrue(b1.typeIndex == 1)
        assertTrue(b1.typeKey.baseType == SimpleIndexedType::class.java)
        assertTrue(b1.typeKey.subType == SITB::class.java)

    }

    @Test
    fun testIndexedTypeAndObject() {
        assertEquals("IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            " * Indexed Type Keys :\n" +
            "}", IndexProvider.dump())

        val a1 = IOATA()
        assertEquals("IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            "  IndexedTypeKey : {0}\n" +
            "  IOATA : {0}\n" +
            " * Indexed Type Keys :\n" +
            "    TypeKey[IndexedTypeKey:SimpleIndexedType:IOATA:0]\n" +
            "}", IndexProvider.dump())
        assertTrue(a1.index == 0)
        assertTrue(a1.objectIndex == 0)
        assertTrue(a1.typeKey.baseType == SimpleIndexedType::class.java)
        assertTrue(a1.typeKey.subType == IOATA::class.java)

        val a2 = IOATA()
        assertEquals("IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            "  IndexedTypeKey : {0}\n" +
            "  IOATA : {0, 1}\n" +
            " * Indexed Type Keys :\n" +
            "    TypeKey[IndexedTypeKey:SimpleIndexedType:IOATA:0]\n" +
            "}", IndexProvider.dump())
        assertTrue(a2.typeIndex == 0)
        assertTrue(a2.objectIndex == 1)
        assertTrue(a2.typeKey.baseType == SimpleIndexedType::class.java)
        assertTrue(a2.typeKey.subType == IOATA::class.java)

        val b1 = IOATB()
        assertEquals("IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            "  IndexedTypeKey : {0, 1}\n" +
            "  IOATA : {0, 1}\n" +
            "  IOATB : {0}\n" +
            " * Indexed Type Keys :\n" +
            "    TypeKey[IndexedTypeKey:SimpleIndexedType:IOATA:0]\n" +
            "    TypeKey[IndexedTypeKey:SimpleIndexedType:IOATB:1]\n" +
            "}", IndexProvider.dump())
        assertTrue(b1.typeIndex == 1)
        assertTrue(b1.objectIndex == 0)
        assertTrue(b1.typeKey.baseType == SimpleIndexedType::class.java)
        assertTrue(b1.typeKey.subType == IOATB::class.java)

        val b2 = IOATB()
        val b3 = IOATB()
        val c1 = IOATC()
        val c2 = IOATC()
        assertEquals("IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            "  IndexedTypeKey : {0, 1, 2}\n" +
            "  IOATA : {0, 1}\n" +
            "  IOATB : {0, 1, 2}\n" +
            "  IOATC : {0, 1}\n" +
            " * Indexed Type Keys :\n" +
            "    TypeKey[IndexedTypeKey:SimpleIndexedType:IOATA:0]\n" +
            "    TypeKey[IndexedTypeKey:SimpleIndexedType:IOATB:1]\n" +
            "    TypeKey[IndexedTypeKey:SimpleIndexedType:IOATC:2]\n" +
            "}", IndexProvider.dump())


    }

}

class SimpleIndexedObject : AbstractIndexed() {
    override val indexer: Indexer = Indexer("SimpleIndexedObject")
    fun dispose() = disposeIndex()
    fun applyNew() = applyNewIndex()
}


abstract class SimpleIndexedType {
    abstract class TypeIndex : AbstractIndexed() {
        override val indexer: Indexer = Indexer("IndexedType")
        fun dispose() = disposeIndex()
        fun applyNew() = applyNewIndex()
    }
}
class SITA : SimpleIndexedType() {
    companion object : TypeIndex()
}
class SITB : SimpleIndexedType() {
    companion object : TypeIndex()
}
class SITC : SimpleIndexedType() {
    companion object : TypeIndex()
}

abstract class IndexedObjectAndType : AbstractIndexed() {
    fun dispose() = disposeIndex()
    fun applyNew() = applyNewIndex()

    abstract class TypeIndex : AbstractIndexed() {
        override val indexer: Indexer = Indexer("IndexedType")
        fun dispose() = disposeIndex()
        fun applyNew() = applyNewIndex()
    }
}

class IOATA : IndexedObjectAndType() {
    override val indexer: Indexer = Indexer("IOATA")
    companion object : TypeIndex()
}
class IOATB : IndexedObjectAndType() {
    override val indexer: Indexer = Indexer("IOATB")
    companion object : TypeIndex()
}
class IOATC : IndexedObjectAndType() {
    override val indexer: Indexer = Indexer("IOATC")
    companion object : TypeIndex()
}

//class SimpleIndexedObject : AbstractIndexedObject(SimpleIndexedObject::class.java)
//
//abstract class SimpleIndexedType(
//    val typeKey: IndexedTypeKey
//) : IndexedType {
////    override val baseType: Class<out IndexedType>
////        get() = SimpleIndexedType::class.java
//    override val typeIndex: Int
//        get() = typeKey.typeIndex
//
//    protected class SimpleIndexedTypeKey(
//        override val subType: Class<out IndexedType>
//    ) : AbstractIndexedTypeKey(SimpleIndexedType::class.java, subType, SimpleIndexedTypeKey::class.java)
//}
//
//class SITA : SimpleIndexedType(typeKey) {
//    companion object {
//        val typeKey: IndexedTypeKey =
//            SimpleIndexedTypeKey(SITA::class.java)
//    }
//}
//
//class SITB : SimpleIndexedType(typeKey) {
//    companion object {
//        val typeKey: IndexedTypeKey =
//            SimpleIndexedTypeKey(SITB::class.java)
//    }
//}
//
//class SITC : SimpleIndexedType(typeKey) {
//    companion object {
//        val typeKey: IndexedTypeKey =
//            SimpleIndexedTypeKey(SITC::class.java)
//    }
//}
//
//
//
//abstract class IndexedObjectAndType(
//    indexedType: Class<out IndexedObject>,
//    val typeKey: IndexedTypeKey
//) : IndexedType, AbstractIndexedObject(indexedType) {
////    override val baseType: Class<out IndexedType>
////        get() = SimpleIndexedType::class.java
//    override val typeIndex: Int
//        get() = typeKey.typeIndex
//
//    protected class BIndexedTypeKey(
//        override val subType: Class<out IndexedType>
//    ) : AbstractIndexedTypeKey(IndexedObjectAndType::class.java, subType, BIndexedTypeKey::class.java)
//}
//
//class IOATA : IndexedObjectAndType(IOATA::class.java, typeKey) {
//    companion object {
//        val typeKey: IndexedTypeKey =
//            BIndexedTypeKey(IOATA::class.java)
//    }
//}
//
//class IOATB : IndexedObjectAndType(IOATB::class.java, typeKey) {
//    companion object {
//        val typeKey: IndexedTypeKey =
//            BIndexedTypeKey(IOATB::class.java)
//    }
//}
//
//class IOATC : IndexedObjectAndType(IOATC::class.java, typeKey) {
//    companion object {
//        val typeKey: IndexedTypeKey =
//            BIndexedTypeKey(IOATC::class.java)
//    }
//}
//
//
//
//abstract class AspectedTestObject(
//    indexedType: Class<out IndexedObject>
//) : IndexedType, AbstractIndexedObject(indexedType) {
////    override val baseType: Class<out IndexedType> = AspectedTestObject::class.java
//
//
//    companion object {
//        private val aspectGroup = IndexedTypeAspectGroup(AspectedTestObject::class.java)
//        val ASPECT_GROUP: AspectGroup = aspectGroup
//    }
//
//    abstract class CType(
//        subType: Class<out IndexedType>
//    ) : Aspect, IndexedType {
//        val typeKey: IndexedAspectTypeKey = aspectGroup.create(subType)
////        override val baseType: Class<out IndexedType> = AspectedTestObject::class.java
//        final override val typeIndex: Int = typeKey.objectIndex
//        final override val aspectType: Class<out IndexedType> = typeKey.aspectType
//        final override val name: String = typeKey.name
//        final override val aspectIndex: Int = typeKey.aspectIndex
//    }
//}
//
//class AspectedCType : AspectedTestObject(AspectedCType::class.java) {
//    override val typeIndex: Int
//        get() = typeKey.typeIndex
//
//    companion object : CType(AspectedCType::class.java)
//}
//



