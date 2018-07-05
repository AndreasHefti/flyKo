package com.inari.util.indexed

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail

import java.util.BitSet

import org.junit.Before
import org.junit.Test


class IndexerTest {

    @Before
    fun init() {
        AA()
        AB()
        AC()
        BA()
        BB()
        BC()
        Indexer.clear()
    }

    @Test
    fun testClassTypeCompare() {
        val aa = AA()
        val bb = BB()
        val aaType = aa.javaClass
        val bbType = bb.javaClass
        assertTrue(AA::class.java == AA::class.java)
        assertTrue(AA::class.java == aa.javaClass)
        assertTrue(AA::class.java == aaType)
        assertFalse(AA::class.java == bbType)

        assertTrue(AA::class.java == AA::class.java)
        assertTrue(AA::class.java == aa.javaClass)
        assertTrue(AA::class.java == aaType)
        assertFalse(AA::class.java == bbType)
    }

    // NOTE: this test was used to find out what BitSet gives on bitset.nextClearBit( 0 ) for a bitset with full cardinality ( all bits are set to true )
    //       if it would give -1 or the next ( not already allocated bit ) index.
    //       conclusion: the next ( not already allocated bit ) index
    @Test
    fun testNextClearBitOnFullBitSet() {
        val bitset = BitSet()

        assertEquals(64, bitset.size().toLong())

        for (i in 0..63) {
            bitset.set(i)
        }

        assertEquals(64, bitset.size().toLong())
        assertEquals(
            "{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63}",
            bitset.toString()
        )

        // !!!
        assertEquals(64, bitset.nextClearBit(0).toLong())

        bitset.set(bitset.nextClearBit(0))
        assertEquals(128, bitset.size().toLong())
    }

    @Test
    fun testIndexedObjects() {
        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                " * Indexed Type Keys :\n" +
                "}",
            Indexer.dump()
        )

        val indexedObject1 = TestIndexedObject()

        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class TestIndexedObject : {0}\n" +
                " * Indexed Type Keys :\n" +
                "}",
            Indexer.dump()
        )
        assertEquals(0, indexedObject1.index)
        assertEquals(1, Indexer.getIndexedObjectSize(TestIndexedObject::class.java).toLong())

        val indexedObject2 = TestIndexedObject()
        val indexedObject3 = TestIndexedObject()
        val indexedObject4 = TestIndexedObject()
        val indexedObject5 = TestIndexedObject()

        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class TestIndexedObject : {0, 1, 2, 3, 4}\n" +
                " * Indexed Type Keys :\n" +
                "}",
            Indexer.dump()
        )
        assertEquals(1, indexedObject2.index)
        assertEquals(2, indexedObject3.index)
        assertEquals(3, indexedObject4.index)
        assertEquals(4, indexedObject5.index)
        assertEquals(5, Indexer.getIndexedObjectSize(TestIndexedObject::class.java).toLong())

        indexedObject2.dispose()
        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class TestIndexedObject : {0, 2, 3, 4}\n" +
                " * Indexed Type Keys :\n" +
                "}",
            Indexer.dump()
        )
        assertEquals(-1, indexedObject2.index)
        assertEquals(5, Indexer.getIndexedObjectSize(TestIndexedObject::class.java).toLong())

        val indexedObject6 = TestIndexedObject()
        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class TestIndexedObject : {0, 1, 2, 3, 4}\n" +
                " * Indexed Type Keys :\n" +
                "}",
            Indexer.dump()
        )
        assertEquals(1, indexedObject6.index)
        assertEquals(5, Indexer.getIndexedObjectSize(TestIndexedObject::class.java).toLong())
    }

    @Test
    fun testIndexedTypeInstantiation() {

        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                " * Indexed Type Keys :\n" +
                "}",
            Indexer.dump()
        )

        val aa1 = AA()

        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "}",
            Indexer.dump()
        )
        val ab1 = AB()

        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0, 1}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "    AB : 1\n" +
                "}",
            Indexer.dump()
        )

        val ac1 = AC()

        assertEquals(0, aa1.indexedTypeKey.index)
        assertEquals(1, ab1.indexedTypeKey.index)
        assertEquals(2, ac1.indexedTypeKey.index)
        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0, 1, 2}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "    AB : 1\n" +
                "    AC : 2\n" +
                "}",
            Indexer.dump()
        )

        val aa2 = AA()
        val ab2 = AB()
        val ac2 = AC()

        assertEquals(0, aa2.indexedTypeKey.index)
        assertEquals(1, ab2.indexedTypeKey.index)
        assertEquals(2, ac2.indexedTypeKey.index)
        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0, 1, 2}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "    AB : 1\n" +
                "    AC : 2\n" +
                "}",
            Indexer.dump()
        )

        val ba1 = BA()

        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0, 1, 2}\n" +
                "  class B\$BIndexedTypeKey : {0}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "    AB : 1\n" +
                "    AC : 2\n" +
                "  B\$BIndexedTypeKey:\n" +
                "    BA : 0\n" +
                "}",
            Indexer.dump()
        )

        val bb1 = BB()

        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0, 1, 2}\n" +
                "  class B\$BIndexedTypeKey : {0, 1}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "    AB : 1\n" +
                "    AC : 2\n" +
                "  B\$BIndexedTypeKey:\n" +
                "    BA : 0\n" +
                "    BB : 1\n" +
                "}",
            Indexer.dump()
        )

        val bc1 = BC()

        assertEquals(0, ba1.indexedTypeKey.index)
        assertEquals(1, bb1.indexedTypeKey.index)
        assertEquals(2, bc1.indexedTypeKey.index)
        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0, 1, 2}\n" +
                "  class B\$BIndexedTypeKey : {0, 1, 2}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "    AB : 1\n" +
                "    AC : 2\n" +
                "  B\$BIndexedTypeKey:\n" +
                "    BA : 0\n" +
                "    BB : 1\n" +
                "    BC : 2\n" +
                "}",
            Indexer.dump()
        )

        val ba2 = BA()
        val bb2 = BB()
        val bc2 = BC()

        assertEquals(0, ba2.indexedTypeKey.index)
        assertEquals(1, bb2.indexedTypeKey.index)
        assertEquals(2, bc2.indexedTypeKey.index)
        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0, 1, 2}\n" +
                "  class B\$BIndexedTypeKey : {0, 1, 2}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "    AB : 1\n" +
                "    AC : 2\n" +
                "  B\$BIndexedTypeKey:\n" +
                "    BA : 0\n" +
                "    BB : 1\n" +
                "    BC : 2\n" +
                "}",
            Indexer.dump()
        )
    }

    @Test
    fun testPreDefinition() {
        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                " * Indexed Type Keys :\n" +
                "}",
            Indexer.dump()
        )

        Indexer.getOrCreateIndexedTypeKey(A.AIndexedTypeKey::class.java, AA::class.java) {A.AIndexedTypeKey(AA::class.java)}

        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "}",
            Indexer.dump()
        )

        Indexer.getOrCreateIndexedTypeKey(A.AIndexedTypeKey::class.java, AB::class.java) {A.AIndexedTypeKey(AB::class.java)}


        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0, 1}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "    AB : 1\n" +
                "}",
            Indexer.dump()
        )

        Indexer.getOrCreateIndexedTypeKey(A.AIndexedTypeKey::class.java, AC::class.java) {A.AIndexedTypeKey(AC::class.java)}

        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0, 1, 2}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "    AB : 1\n" +
                "    AC : 2\n" +
                "}",
            Indexer.dump()
        )

        Indexer.getOrCreateIndexedTypeKey(A.AIndexedTypeKey::class.java, AA::class.java) {A.AIndexedTypeKey(AA::class.java)}
        Indexer.getOrCreateIndexedTypeKey(A.AIndexedTypeKey::class.java, AB::class.java) {A.AIndexedTypeKey(AB::class.java)}
        Indexer.getOrCreateIndexedTypeKey(A.AIndexedTypeKey::class.java, AC::class.java) {A.AIndexedTypeKey(AC::class.java)}

        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0, 1, 2}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "    AB : 1\n" +
                "    AC : 2\n" +
                "}",
            Indexer.dump()
        )

        Indexer.getIndexedTypeKey(B.BIndexedTypeKey::class.java, BA::class.java)

        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0, 1, 2}\n" +
                "  class B\$BIndexedTypeKey : {0}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "    AB : 1\n" +
                "    AC : 2\n" +
                "  B\$BIndexedTypeKey:\n" +
                "    BA : 0\n" +
                "}",
            Indexer.dump()
        )

        Indexer.getIndexedTypeKey(B.BIndexedTypeKey::class.java, BB::class.java)

        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0, 1, 2}\n" +
                "  class B\$BIndexedTypeKey : {0, 1}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "    AB : 1\n" +
                "    AC : 2\n" +
                "  B\$BIndexedTypeKey:\n" +
                "    BA : 0\n" +
                "    BB : 1\n" +
                "}",
            Indexer.dump()
        )

        Indexer.getIndexedTypeKey(B.BIndexedTypeKey::class.java, BC::class.java)

        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0, 1, 2}\n" +
                "  class B\$BIndexedTypeKey : {0, 1, 2}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "    AB : 1\n" +
                "    AC : 2\n" +
                "  B\$BIndexedTypeKey:\n" +
                "    BA : 0\n" +
                "    BB : 1\n" +
                "    BC : 2\n" +
                "}",
            Indexer.dump()
        )

        Indexer.getIndexedTypeKey(B.BIndexedTypeKey::class.java, BA::class.java)
        Indexer.getIndexedTypeKey(B.BIndexedTypeKey::class.java, BB::class.java)
        Indexer.getIndexedTypeKey(B.BIndexedTypeKey::class.java, BC::class.java)

        assertEquals(
            "IndexProvider : {\n" +
                " * Indexed Objects :\n" +
                "  class A\$AIndexedTypeKey : {0, 1, 2}\n" +
                "  class B\$BIndexedTypeKey : {0, 1, 2}\n" +
                " * Indexed Type Keys :\n" +
                "  A\$AIndexedTypeKey:\n" +
                "    AA : 0\n" +
                "    AB : 1\n" +
                "    AC : 2\n" +
                "  B\$BIndexedTypeKey:\n" +
                "    BA : 0\n" +
                "    BB : 1\n" +
                "    BC : 2\n" +
                "}",
            Indexer.dump()
        )
    }

    @Test
    fun testWrongPreDefinition() {
        try {
            Indexer.getOrCreateIndexedTypeKey(A.AIndexedTypeKey::class.java, BA::class.java) {A.AIndexedTypeKey(AA::class.java)}
            fail("IllegalArgumentException expected")
        } catch (e: IllegalArgumentException) {
            assertEquals("IndexedType mismatch: subType: class com.inari.util.indexed.BA is not a valid substitute of base type: class com.inari.util.indexed.A", e.message)
        }

        try {
            Indexer.getOrCreateIndexedTypeKey(B.BIndexedTypeKey::class.java, AA::class.java) {B.BIndexedTypeKey(BA::class.java)}
            fail("IllegalArgumentException expected")
        } catch (e: IllegalArgumentException) {
            assertEquals("IndexedType mismatch: subType: class com.inari.util.indexed.AA is not a valid substitute of base type: interface com.inari.util.indexed.B", e.message)
        }

    }
}




