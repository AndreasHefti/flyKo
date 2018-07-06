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
        assertTrue(o1.objectIndex == 0)
        assertTrue(o2.objectIndex == 1)
        assertTrue(o3.objectIndex == 2)
        assertTrue(o1.indexedType == SimpleIndexedObject::class.java)

        o1.disposeIndex()
        assertEquals("IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            "  SimpleIndexedObject : {1, 2}\n" +
            " * Indexed Type Keys :\n" +
            "}", IndexProvider.dump())
        assertTrue(o1.objectIndex == -1)

        val o4 = SimpleIndexedObject()
        assertTrue(o4.objectIndex == 0)

        o1.loadIndex()
        assertTrue(o1.objectIndex == 3)
        o1.loadIndex()
        assertTrue(o1.objectIndex == 3)
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
        assertTrue(a1.typeIndex == 0)
        assertTrue(a1.typeKey.baseType == SimpleIndexedType::class.java)
        assertTrue(a1.typeKey.subType == SITA::class.java)

        val a2 = SITA()
        assertEquals("IndexProvider : {\n" +
            " * Indexed Objects :\n" +
            "  SimpleIndexedTypeKey : {0}\n" +
            " * Indexed Type Keys :\n" +
            "    TypeKey[SimpleIndexedTypeKey:SimpleIndexedType:SITA:0]\n" +
            "}", IndexProvider.dump())
        assertTrue(a2.typeIndex == 0)
        assertTrue(a2.typeKey.baseType == SimpleIndexedType::class.java)
        assertTrue(a2.typeKey.subType == SITA::class.java)

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
        assertTrue(a1.typeIndex == 0)
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

class SimpleIndexedObject : AbstractIndexedObject(SimpleIndexedObject::class.java)

abstract class SimpleIndexedType : IIndexedType {
    override val typeIndex: Int
        get() = typeKey.objectIndex

    protected class SimpleIndexedTypeKey(
        override val subType: Class<out IIndexedType>
    ) : AbstractIndexedTypeKey(SimpleIndexedType::class.java, subType, SimpleIndexedTypeKey::class.java)
}

class SITA : SimpleIndexedType() {
    override val typeKey: NewIndexedTypeKey =
        SITA.typeKey

    companion object {
        val typeKey: NewIndexedTypeKey =
            SimpleIndexedTypeKey(SITA::class.java)
    }
}

class SITB : SimpleIndexedType() {
    override val typeKey: NewIndexedTypeKey =
        SITB.typeKey

    companion object {
        val typeKey: NewIndexedTypeKey =
            SimpleIndexedTypeKey(SITB::class.java)
    }
}

class SITC : SimpleIndexedType() {
    override val typeKey: NewIndexedTypeKey =
        SITC.typeKey

    companion object {
        val typeKey: NewIndexedTypeKey =
            SimpleIndexedTypeKey(SITC::class.java)
    }
}



abstract class IndexedObjectAndType(
    indexedType: Class<out IIndexedObject>
) : IIndexedType, AbstractIndexedObject(indexedType) {
    override val typeIndex: Int
        get() = typeKey.objectIndex

    protected class IndexedTypeKey(
        override val subType: Class<out IIndexedType>
    ) : AbstractIndexedTypeKey(SimpleIndexedType::class.java, subType, IndexedTypeKey::class.java)
}

class IOATA : IndexedObjectAndType(IOATA::class.java) {
    override val typeKey =
        IOATA.typeKey

    companion object {
        val typeKey: NewIndexedTypeKey =
            IndexedTypeKey(IOATA::class.java)
    }
}

class IOATB : IndexedObjectAndType(IOATB::class.java) {
    override val typeKey =
        IOATB.typeKey

    companion object {
        val typeKey: NewIndexedTypeKey =
            IndexedTypeKey(IOATB::class.java)
    }
}

class IOATC : IndexedObjectAndType(IOATC::class.java) {
    override val typeKey =
        IOATC.typeKey

    companion object {
        val typeKey: NewIndexedTypeKey =
            IndexedTypeKey(IOATC::class.java)
    }
}