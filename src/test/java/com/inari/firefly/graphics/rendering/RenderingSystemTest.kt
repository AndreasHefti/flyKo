package com.inari.firefly.graphics.rendering

import com.inari.firefly.GraphicsMock
import com.inari.firefly.TestApp
import com.inari.firefly.asset.AssetSystem
import com.inari.firefly.asset.TestAsset
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.graphics.view.ViewSystem
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RenderingSystemTest {

    @Test
    fun testSystemInit() {
        TestApp
        RenderingSystem

        assertTrue(
            RenderingSystem.renderer[SimpleSpriteRenderer.index()] === SimpleSpriteRenderer
        )
    }

    @Test
    fun testrenderSprite() {
        TestApp
        AssetSystem
        ViewSystem
        RenderingSystem
        EntitySystem

        val assetId = TestAsset.buildAndActivate {
            ff_Name = "Test"
        }

        Entity.buildAndActivate {
            with(ETransform) {
                ff_View.index = 0
                ff_PosX = 1f
                ff_PivotY = 2f
            }
            with(ESprite) {
                ff_Sprite.id = assetId
            }
        }

        assertEquals("[]", GraphicsMock.log())

        TestApp.render()

        assertEquals(
            "[startRendering::View(View(baseView=true, controllerRef=-1, bounds=[x=0,y=0,width=100,height=100], worldPosition=[x=0.0,y=0.0], clearColor=[r=0.0,g=0.0,b=0.0,a=1.0], tintColor=[r=1.0,g=1.0,b=1.0,a=1.0], blendMode=NONE, zoom=1.0, fboScaler=4.0)), " +
                "renderSprite::Sprite(ESprite(spriteRef=1, shaderRef=-1, blend=NONE, tint=[r=1.0,g=1.0,b=1.0,a=1.0], ), " +
                "endRendering::View(View(baseView=true, controllerRef=-1, bounds=[x=0,y=0,width=100,height=100], worldPosition=[x=0.0,y=0.0], clearColor=[r=0.0,g=0.0,b=0.0,a=1.0], tintColor=[r=1.0,g=1.0,b=1.0,a=1.0], blendMode=NONE, zoom=1.0, fboScaler=4.0)), " +
                "flush]",
            GraphicsMock.log())
    }
}