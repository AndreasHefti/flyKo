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
            name = "BASE_TEXTURE"
            resourceName = "firefly/inari.png"
        }

        TextureAsset.buildAndActivate {
            name = "ALPHA_MASK"
            resourceName = "firefly/alphaMaskCircle.png"
        }

        SpriteAsset.buildAndActivate {
            name = "TestSprite"
            texture("BASE_TEXTURE")
            textureRegion(100,0,100,100)
        }

        ShaderAsset.buildAndActivate {
            name="Shader"
            fragShaderProgram = "#ifdef GL_ES\n" +
                    "    precision mediump float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "uniform sampler2D u_textureMask;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec4 color = normalize(texture2D(u_texture, v_texCoords));\n" +
                    "    gl_FragColor = texture2D(u_textureMask , v_texCoords);\n" +
                    "}"
            shaderInit = { shaderInit ->
                shaderInit.setTexture("u_textureMask", "ALPHA_MASK")
            }
        }

        Entity.buildAndActivate {
            component(ETransform) {
                position(10,0)
            }
            component(ESprite) {
                sprite("TestSprite")
                shader("Shader")
                        
            }
        }

        Entity.buildAndActivate {
            component(ETransform) {
                position(200,200)
            }
            component(ESprite) {
                sprite("TestSprite")
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