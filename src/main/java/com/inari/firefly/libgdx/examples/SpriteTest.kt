package com.inari.firefly.libgdx.examples

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.inari.firefly.entity.Entity
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.TextureAsset
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.graphics.sprite.SpriteAsset
import com.inari.firefly.libgdx.GDXApp
import com.inari.firefly.libgdx.GDXAppAdapter

class SpriteTest : GDXAppAdapter() {

    override val title: String = "IntroTest"

    override fun init() {

        TextureAsset.build {
            ff_Name = "TEST"
            ff_ResourceName = "firefly/inari.png"
        }

        SpriteAsset.buildAndActivate {
            ff_Name = "TestSprite"
            ff_Texture("TEST")
            ff_TextureRegion(0,0,100,100)
        }

        Entity.buildAndActivate {
            ff_With(ETransform) {
                ff_Position(0,0)
            }
            ff_With(ESprite) {
                ff_Sprite("TestSprite")
            }
        }

    }

}

fun main(args: Array<String>) {
    try {
        val config = LwjglApplicationConfiguration()
        config.resizable = false
        config.width = 704
        config.height = 480
        config.fullscreen = false
        LwjglApplication(SpriteTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}