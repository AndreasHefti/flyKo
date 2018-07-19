package com.inari.util.indexed

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IndexerTest {

    @Before
    fun init() {
        Indexer.clearAll()
    }

    @Test
    fun testSimpleIndexedObject() {
        assertEquals(
            "Indexer : {\n" +
                "}",
            Indexer.dump())

        val o1 = SimpleIndexedObject()
        val o2 = SimpleIndexedObject()
        val o3 = SimpleIndexedObject()

        assertEquals("Indexer : {\n" +
            "  SimpleIndexedObject : \n" +
            "    0\n" +
            "    1\n" +
            "    2\n" +
            "}", Indexer.dump())
        assertTrue(o1.index == 0)
        assertTrue(o2.index == 1)
        assertTrue(o3.index == 2)
        assertTrue(o1.indexedTypeName == "SimpleIndexedObject")

        o1.dispose()
        assertEquals("Indexer : {\n" +
            "  SimpleIndexedObject : \n" +
            "    1\n" +
            "    2\n" +
            "}", Indexer.dump())
        assertTrue(o1.index == -1)

        val o4 = SimpleIndexedObject()
        assertTrue(o4.index == 0)

        o1.applyNew()
        assertTrue(o1.index == 3)
        o1.applyNew()
        assertTrue(o1.index == 3)
        assertEquals("Indexer : {\n" +
            "  SimpleIndexedObject : \n" +
            "    0\n" +
            "    1\n" +
            "    2\n" +
            "    3\n" +
            "}", Indexer.dump())
    }

    @Test
    fun testSimpleIndexedType() {
        assertEquals("Indexer : {\n" +
            "}", Indexer.dump())

        val a1 = SITA()
        assertEquals("Indexer : {\n" +
            "  SimpleIndexedType : \n" +
            "    0:SITA\n" +
            "}", Indexer.dump())
        assertTrue(SITA.index ==  0)
        assertTrue(SITA.indexedTypeName == "SimpleIndexedType")

        val a2 = SITA()
        assertEquals("Indexer : {\n" +
            "  SimpleIndexedType : \n" +
            "    0:SITA\n" +
            "}", Indexer.dump())
        assertTrue(SITA.index == 0)

        val b1 = SITB()
        assertEquals("Indexer : {\n" +
            "  SimpleIndexedType : \n" +
            "    0:SITA\n" +
            "    1:SITB\n" +
            "}", Indexer.dump())
        assertTrue(SITB.index == 1)
        assertEquals("SimpleIndexedType", SITB.indexedTypeName)

        val c1 = SITC()
        assertEquals("Indexer : {\n" +
            "  SimpleIndexedType : \n" +
            "    0:SITA\n" +
            "    1:SITB\n" +
            "    2:SITC\n" +
            "}", Indexer.dump())
        assertTrue(SITC.index == 2)
        assertEquals("SimpleIndexedType", SITC.indexedTypeName)



        SITC.dispose()
        assertEquals("Indexer : {\n" +
            "  SimpleIndexedType : \n" +
            "    0:SITA\n" +
            "    1:SITB\n" +
            "}", Indexer.dump())
        assertTrue(SITC.index == -1)
        assertEquals("SimpleIndexedType", SITC.indexedTypeName)

    }

    @Test
    fun testIndexedTypeAndObject() {
        assertEquals("Indexer : {\n" +
            "}", Indexer.dump())

        val a1 = IOATA()
        assertEquals("Indexer : {\n" +
            "  IndexedType : \n" +
            "    0:IOATA\n" +
            "  Object:IOATA : \n" +
            "    0\n" +
            "}", Indexer.dump())
        assertTrue(a1.index == 0)
        assertTrue(IOATA.index == 0)

        val a2 = IOATA()
        assertEquals("Indexer : {\n" +
            "  IndexedType : \n" +
            "    0:IOATA\n" +
            "  Object:IOATA : \n" +
            "    0\n" +
            "    1\n" +
            "}", Indexer.dump())
        assertTrue(a2.index == 1)
        assertTrue(IOATA.index == 0)

        val b1 = IOATB()
        assertEquals("Indexer : {\n" +
            "  IndexedType : \n" +
            "    0:IOATA\n" +
            "    1:IOATB\n" +
            "  Object:IOATA : \n" +
            "    0\n" +
            "    1\n" +
            "  Object:IOATB : \n" +
            "    0\n" +
            "}", Indexer.dump())
        assertTrue(b1.index == 0)
        assertTrue(IOATB.index == 1)

        val b2 = IOATB()
        val b3 = IOATB()
        val c1 = IOATC()
        val c2 = IOATC()
        assertEquals("Indexer : {\n" +
            "  IndexedType : \n" +
            "    0:IOATA\n" +
            "    1:IOATB\n" +
            "    2:IOATC\n" +
            "  Object:IOATA : \n" +
            "    0\n" +
            "    1\n" +
            "  Object:IOATB : \n" +
            "    0\n" +
            "    1\n" +
            "    2\n" +
            "  Object:IOATC : \n" +
            "    0\n" +
            "    1\n" +
            "}", Indexer.dump())


    }

}

class SimpleIndexedObject : AbstractIndexed("SimpleIndexedObject") {
    fun dispose() = disposeIndex()
    fun applyNew() = applyNewIndex()
}


abstract class SimpleIndexedType {
    abstract class TypeIndex(subTypeName: String) : AbstractIndexed("SimpleIndexedType", subTypeName) {
        fun dispose() = disposeIndex()
        fun applyNew() = applyNewIndex()
    }
}
class SITA : SimpleIndexedType() {
    companion object : TypeIndex("SITA")
}
class SITB : SimpleIndexedType() {
    companion object : TypeIndex("SITB")
}
class SITC : SimpleIndexedType() {
    companion object : TypeIndex("SITC")
}

abstract class IndexedObjectAndType(
    objectIndexerName: String
) : AbstractIndexed(objectIndexerName) {
    fun dispose() = disposeIndex()
    fun applyNew() = applyNewIndex()

    abstract class TypeIndex(subTypeName: String) : AbstractIndexed("IndexedType", subTypeName) {
        fun dispose() = disposeIndex()
        fun applyNew() = applyNewIndex()
    }
}

class IOATA : IndexedObjectAndType("Object:IOATA") {
    companion object : TypeIndex("IOATA")
}
class IOATB : IndexedObjectAndType("Object:IOATB") {
    companion object : TypeIndex("IOATB")
}
class IOATC : IndexedObjectAndType("Object:IOATC") {
    companion object : TypeIndex("IOATC")
}




