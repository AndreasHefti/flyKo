package com.inari.firefly.asset

import com.inari.firefly.NO_NAME
import com.inari.firefly.FFContext
import com.inari.firefly.TestApp
import org.junit.Before
import org.junit.Test
import kotlin.test.*

class AssetSystemTest {


    @Before
    fun init() {
        TestApp
        AssetSystem.clearSystem()
    }

    @Test
    fun assetCreation() {

        assertTrue { AssetSystem.assets.map.isEmpty }
        assertTrue { AssetSystem.assets == FFContext.mapper(Asset) }

        val emptyAssetId = TestAsset.build {}

        assertEquals(
            "CompId(index=0, typeKey=SystemComponent:Asset)",
            emptyAssetId.toString()
        )

        assertFalse { AssetSystem.assets.map.isEmpty }

        val emptyAsset = FFContext.get<Asset>(emptyAssetId)
        assertNotNull(emptyAsset)
        assertEquals(
            "TestAsset(name='[[NO_NAME]]', ff_Param1='', ff_Param2=0.0, instanceId=-1)",
            emptyAsset.toString()
        )

        try {
            AssetSystem.assets.get(NO_NAME)
            fail("Exception expected here")
        } catch (e: Exception) {
            assertEquals("Component: SystemComponent:Asset for name: [[NO_NAME]] not found", e.message)
        }

        val assetId = TestAsset.build {
            ff_Name = "testName"
            ff_Param1 = "param1"
            ff_Param2 = 1.45f
        }

        assertNotNull(assetId)
        val assetById = FFContext.get<Asset>(assetId)
        val assetByName = FFContext.get(Asset, "testName")
        assertEquals(assetById, assetByName)
    }

    @Test
    fun simpleLifeCycle() {
        val testEvents = StringBuilder()
        FFContext.registerListener<AssetEventListener>(
            AssetEvent,
            { _, type -> testEvents.append("|").append(type.toString())}
        )
        assertEquals("", testEvents.toString())

        val assetId = TestAsset.build {
            ff_Name = "testName"
            ff_Param1 = "param1"
            ff_Param2 = 1.45f
        }

        assertEquals(
            "CompId(index=0, typeKey=SystemComponent:Asset)",
            assetId.toString()
        )
        assertEquals(
            "|ASSET_CREATED",
            testEvents.toString()
        )

        FFContext.activate(assetId)
        assertEquals(
            "|ASSET_CREATED|ASSET_LOADED",
            testEvents.toString()
        )
        FFContext.deactivate(assetId)
        assertEquals(
            "|ASSET_CREATED|ASSET_LOADED|ASSET_DISPOSED",
            testEvents.toString()
        )
        FFContext.activate(assetId)
        assertEquals(
            "|ASSET_CREATED|ASSET_LOADED|ASSET_DISPOSED|ASSET_LOADED",
            testEvents.toString()
        )
        FFContext.delete(assetId)
        assertEquals(
            "|ASSET_CREATED|ASSET_LOADED|ASSET_DISPOSED|ASSET_LOADED|ASSET_DISPOSED|ASSET_DELETED",
            testEvents.toString()
        )
    }

    @Test
    fun lifeCycleWithDependentAssets() {
        val testEvents = StringBuilder()
        FFContext.registerListener<AssetEventListener>(
            AssetEvent,
            { id, type -> testEvents.append("|id=").append(id.index).append(":").append(type.toString())}
        )
        assertEquals("", testEvents.toString())

        val asset1 = TestAsset.build {
            ff_Name = "parentAsset"
            ff_Param1 = "parent"
            ff_Param2 = 1.45f
        }
        assertEquals(
            "|id=0:ASSET_CREATED",
            testEvents.toString()
        )

        val asset2 = TestAsset.build {
            ff_DependsOn = "parentAsset"
            ff_Name = "childAsset"
            ff_Param1 = "child"
            ff_Param2 = 1.45f
        }
        assertEquals(
            "|id=0:ASSET_CREATED" +
            "|id=1:ASSET_CREATED",
            testEvents.toString()
        )
        assertEquals(
            "TestAsset(name='childAsset', ff_Param1='child', ff_Param2=1.45, instanceId=-1) dependsOn=NamedReference(index=0, name='parentAsset')",
            FFContext.get<Asset>(asset2).toString()
        )

        FFContext.activate(asset2)
        assertEquals(
            "|id=0:ASSET_CREATED" +
            "|id=1:ASSET_CREATED" +
            "|id=0:ASSET_LOADED" +
            "|id=1:ASSET_LOADED",
            testEvents.toString()
        )
        FFContext.deactivate(asset1)
        assertEquals(
            "|id=0:ASSET_CREATED" +
            "|id=1:ASSET_CREATED" +
            "|id=0:ASSET_LOADED" +
            "|id=1:ASSET_LOADED" +
            "|id=1:ASSET_DISPOSED" +
            "|id=0:ASSET_DISPOSED",
            testEvents.toString()
        )
    }
}