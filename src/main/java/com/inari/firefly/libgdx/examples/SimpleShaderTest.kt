package com.inari.firefly.libgdx.examples

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.entity.Entity
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.ShaderAsset
import com.inari.firefly.graphics.TextureAsset
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.graphics.sprite.SpriteAsset
import com.inari.firefly.libgdx.DesktopAppAdapter

class SimpleShaderTest : DesktopAppAdapter() {

    override val title: String = "SimpleShaderTest"

    override fun init() {



        TextureAsset.build {
            ff_Name = "BASE_TEXTURE"
            ff_ResourceName = "firefly/inari.png"
        }

        TextureAsset.buildAndActivate {
            ff_Name = "ALPHA_MASK"
            ff_ResourceName = "firefly/alphaMaskCircle.png"
        }

        SpriteAsset.buildAndActivate {
            ff_Name = "TestSprite"
            ff_Texture("BASE_TEXTURE")
            ff_TextureRegion(0,0,100,100)
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
                    "uniform sampler2D u_textureMask;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec4 color = texture2D(u_texture, v_texCoords);\n" +
                    "    color.a = texture2D(u_textureMask, v_texCoords).r;\n" +
                    "    gl_FragColor = v_color * color;\n" +
                    "}"
            ff_ShaderInit = { shaderInit ->
                shaderInit.setTexture("u_textureMask", "ALPHA_MASK")
            }
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
        val config = Lwjgl3ApplicationConfiguration()
        config.setResizable(true)
        config.setWindowedMode(800, 600)
        Lwjgl3Application(SimpleShaderTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}