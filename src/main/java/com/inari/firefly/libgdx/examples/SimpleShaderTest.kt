package com.inari.firefly.libgdx.examples

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.inari.firefly.entity.Entity
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.ShaderAsset
import com.inari.firefly.graphics.TextureAsset
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.graphics.sprite.SpriteAsset
import com.inari.firefly.libgdx.GDXAppAdapter

class SimpleShaderTest : GDXAppAdapter() {

    override val title: String = "SimpleShaderTest"

    override fun init() {



        TextureAsset.build {
            ff_Name = "TEST"
            ff_ResourceName = "firefly/inari.png"
        }

        SpriteAsset.buildAndActivate {
            ff_Name = "TestSprite"
            ff_Texture("TEST")
            ff_TextureRegion(0,0,200,100)
        }

        ShaderAsset.buildAndActivate {
            ff_Name="Shader"
            ff_FragShaderProgram = "#ifdef GL_ES\n" +
                    "    precision mediump float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec4 color = texture2D(u_texture, v_texCoords);\n" +
                    "    color.rgb = 1.0 - color.rgb;\n" +
                    "    gl_FragColor = v_color * color;\n" +
                    "}"
        }

        Entity.buildAndActivate {
            ff_With(ETransform) {
                ff_Position(0,0)
            }
            ff_With(ESprite) {
                ff_Sprite("TestSprite")
                ff_Shader("Shader")
            }
        }

        Entity.buildAndActivate {
            ff_With(ETransform) {
                ff_Position(200,200)
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
        LwjglApplication(SimpleShaderTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}