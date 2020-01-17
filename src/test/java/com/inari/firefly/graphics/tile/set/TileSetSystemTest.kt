package com.inari.firefly.graphics.tile.set

import com.inari.firefly.BASE_VIEW
import com.inari.firefly.FFContext
import com.inari.firefly.TestApp
import com.inari.firefly.asset.AssetSystem
import com.inari.firefly.composite.CompositeSystem
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.TextureAsset
import com.inari.firefly.graphics.sprite.SpriteSetAsset
import com.inari.firefly.graphics.tile.set.TileSet
import com.inari.firefly.graphics.tile.set.TileSetActivation
import com.inari.firefly.graphics.tile.set.TileSetSystem
import com.inari.firefly.graphics.view.Layer
import com.inari.firefly.graphics.view.ViewSystem
import com.inari.firefly.physics.animation.AnimationSystem
import com.inari.firefly.physics.contact.ContactSystem.UNDEFINED_CONTACT_TYPE
import com.inari.firefly.physics.contact.ContactSystem.UNDEFINED_MATERIAL
import com.inari.util.geom.BitMask
import com.inari.util.graphics.RGBColor
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TileSetSystemTest {

    @Before
    fun init() {
        TestApp
        AssetSystem.clearSystem()
        ViewSystem.clearSystem()
        EntitySystem.clearSystem()
        CompositeSystem.clearSystem()
        AnimationSystem.clearSystem()
    }

    @Test
    fun testOneTileSet() {

        Layer.buildAndActivate {
            ff_Name = "testLayer"
            ff_View(BASE_VIEW)
        }

        TextureAsset.build {
            ff_Name = "TestTextureAsset"
            ff_ResourceName = "firefly/fireflyMicroFont.png"
        }

        TileSet.build {
            ff_Name = "TestTileSet1"
            ff_TextureAsset("TestTextureAsset")
            ff_withTile {
                ff_withSprite {
                    ff_textureBounds(0,0, 8, 8)
                    ff_HFlip = true
                }
                ff_withAnimation {
                    ff_withFrame {
                        ff_Interval = 100
                        ff_withSprite {
                            ff_Name = "s1"
                            ff_textureBounds(8,0, 8, 8)
                        }
                        ff_withSprite {
                            ff_Name = "s2"
                            ff_textureBounds(16,0, 8, 8)
                        }
                    }
                }
            }
            ff_withTile {
                ff_withSprite {
                    ff_textureBounds(0,0, 8, 8)
                }
                ff_BlendMode = BlendMode.ADDITIVE_ALPHA
                ff_ContactMask = BitMask()
                ff_ContactType = UNDEFINED_CONTACT_TYPE
                ff_Material = UNDEFINED_MATERIAL
                ff_TintColor = RGBColor.WHITE
            }
            ff_withTile {
                ff_withSprite {
                    ff_textureBounds(0,0, 8, 8)
                }
            }
            ff_ActivationResolver = { _ -> TileSetActivation.of {
                ff_View(ViewSystem.baseView)
                ff_withLayer {
                    ff_Layer("testLayer")
                    ff_BlendMode = BlendMode.ADDITIVE_ALPHA
                    ff_TintColor = RGBColor.WHITE
                }
            } }
        }



        // after TileSet creation a SpriteSetAsset with the same name must have been created
        val spriteSetAsset: SpriteSetAsset = FFContext[SpriteSetAsset, "TestTileSet1"]
        assertNotNull(spriteSetAsset)
        // The SpriteSetAsset should not be active/loaded yet
        assertFalse(FFContext.isActive(SpriteSetAsset, "TestTileSet1"))

        // activate the TileSet should also activate the SpriteSetAsset and the TextureAsset
        FFContext.activate(TileSet, "TestTileSet1")
        assertTrue(FFContext.isActive(TileSet, "TestTileSet1"))
        assertTrue(FFContext.isActive(SpriteSetAsset, "TestTileSet1"))
        assertTrue(FFContext.isActive(TextureAsset, "TestTextureAsset"))

        assertNotNull(TileSetSystem["TestTileSet1"])
        assertEquals(0, TileSetSystem.getEntityId(0, 0))
        assertEquals(1, TileSetSystem.getEntityId(1, 0))
        assertEquals(2, TileSetSystem.getEntityId(2, 0))
        assertEquals(-1, TileSetSystem.getEntityId(3, 0))

        // creating and activate a second tileset
        TileSet.build {
            ff_Name = "TestTileSet2"
            ff_TextureAsset("TestTextureAsset")
            ff_withTile {
                ff_withSprite {
                    ff_textureBounds(0,0, 8, 8)
                }
            }
            ff_withTile {
                ff_withSprite {
                    ff_textureBounds(8,0, 8, 8)
                }
            }
            ff_withTile {
                ff_withSprite {
                    ff_textureBounds(16,0, 8, 8)
                }
            }
            ff_ActivationResolver = { _ -> TileSetActivation.of {
                ff_View(ViewSystem.baseView)
                ff_withLayer {
                    ff_Layer("testLayer")
                }
            } }
        }

        FFContext.activate(TileSet, "TestTileSet2")
        assertTrue(FFContext.isActive(TileSet, "TestTileSet2"))
        assertTrue(FFContext.isActive(SpriteSetAsset, "TestTileSet2"))
        assertTrue(FFContext.isActive(TextureAsset, "TestTextureAsset"))

        assertNotNull(TileSetSystem["TestTileSet2"])
        assertEquals(0, TileSetSystem.getEntityId(0, 0))
        assertEquals(1, TileSetSystem.getEntityId(1, 0))
        assertEquals(2, TileSetSystem.getEntityId(2, 0))
        assertEquals(3, TileSetSystem.getEntityId(3, 0))
        assertEquals(4, TileSetSystem.getEntityId(4, 0))
        assertEquals(5, TileSetSystem.getEntityId(5, 0))
        assertEquals(-1, TileSetSystem.getEntityId(6, 0))

        // now deactivating the first tileset should result in a mapping shift
        FFContext.deactivate(TileSet, "TestTileSet1")
        assertFalse(FFContext.isActive(TileSet, "TestTileSet1"))
        assertTrue(FFContext.isActive(TileSet, "TestTileSet2"))
        assertTrue(FFContext.isActive(TextureAsset, "TestTextureAsset"))

        assertEquals(3, TileSetSystem.getEntityId(0, 0))
        assertEquals(4, TileSetSystem.getEntityId(1, 0))
        assertEquals(5, TileSetSystem.getEntityId(2, 0))
    }
}