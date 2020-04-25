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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RenderingSystemTest {

    @Test
    fun testSystemInit() {
        TestApp
        RenderingSystem.clearSystem()

        assertTrue(
            RenderingSystem[SimpleSpriteRenderer] === SimpleSpriteRenderer.instance
        )
    }

    @Test
    fun testRenderSprite() {
        TestApp
        AssetSystem.clearSystem()
        ViewSystem.clearSystem()
        RenderingSystem.clearSystem()
        EntitySystem.clearSystem()
        GraphicsMock.clearLogs()

        val assetId = TestAsset.buildAndActivate {
            ff_Name = "Test"
        }

        Entity.buildAndActivate {
            ff_With(ETransform) {
                ff_View(0)
                ff_Pivot.x = 1f
                ff_Pivot.y = 2f
            }
            ff_With(ESprite) {
                ff_Sprite( assetId)
            }
        }

        assertEquals("[]", GraphicsMock.log())

        TestApp.render()

        assertEquals(
            "[startRendering::ViewData(" +
                "baseView=true, " +
                "bounds=[x=0,y=0,width=100,height=100], " +
                "worldPosition=[x=0.0,y=0.0], " +
                "clearColor=[r=0.0,g=0.0,b=0.0,a=1.0], " +
                "tintColor=[r=1.0,g=1.0,b=1.0,a=1.0], " +
                "blendMode=NONE, " +
                "zoom=1.0, " +
                "fboScaler=1.0), " +
                "renderSprite::Sprite(SpriteRenderable(" +
                "spriteId=1, " +
                "tintColor=[r=1.0,g=1.0,b=1.0,a=1.0], " +
                "blendMode=NONE, " +
                "shaderId=-1)), " +
                "endRendering::ViewData(" +
                "baseView=true, " +
                "bounds=[x=0,y=0,width=100,height=100], " +
                "worldPosition=[x=0.0,y=0.0], " +
                "clearColor=[r=0.0,g=0.0,b=0.0,a=1.0], " +
                "tintColor=[r=1.0,g=1.0,b=1.0,a=1.0], " +
                "blendMode=NONE, " +
                "zoom=1.0, " +
                "fboScaler=1.0), " +
                "flush]",
            GraphicsMock.log())
    }
}