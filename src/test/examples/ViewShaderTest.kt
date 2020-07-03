import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.entity.Entity
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.ShaderAsset
import com.inari.firefly.graphics.TextureAsset
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.graphics.sprite.SpriteAsset
import com.inari.firefly.graphics.view.View
import com.inari.firefly.libgdx.DesktopAppAdapter

class ViewShaderTest : DesktopAppAdapter() {

    override val title: String = "ViewShaderTest"

    override fun init() {

        TextureAsset.build {
            ff_Name = "TEST"
            ff_ResourceName = "firefly/inari.png"
        }

        SpriteAsset.buildAndActivate {
            ff_Name = "TestSprite"
            ff_Texture("TEST")
            ff_TextureRegion(0,0,400,200)
        }

        ShaderAsset.buildAndActivate {
            ff_Name="Shader"

            ff_FragShaderProgram = "#ifdef GL_ES\n" +
                    "    precision mediump float;\n" +
                    "#endif\n" +
                    "\n" +
                    "    varying vec2      v_texCoords;\n" +
                    "    uniform sampler2D u_texture;\n" +
                    "    uniform float     u_distortion;\n" +
                    "    uniform float     u_stripe;\n" +
                    "    uniform float     u_rgbshift;\n" +
                    "    \n" +
                    "    void main()\n" +
                    "    {\n" +
                    "        // distortion\n" +
                    "        vec2 ndc_pos = v_texCoords;\n" +
                    "        vec2 testVec = ndc_pos.xy / max(abs(ndc_pos.x), abs(ndc_pos.y));\n" +
                    "        float len = max(1.0,length( testVec ));\n" +
                    "        ndc_pos *= mix(1.0, mix(1.0,len,max(abs(ndc_pos.x), abs(ndc_pos.y))), u_distortion);\n" +
                    "        vec2 texCoord = vec2(ndc_pos.s, -ndc_pos.t) * 0.5 + 0.5;\n" +
                    "\n" +
                    "        // stripes\n" +
                    "        float stripTile = texCoord.t * mix(10.0, 100.0, u_stripe);\n" +
                    "        float stripFac = 1.0 + 0.25 * u_stripe * (step(0.5, stripTile-float(int(stripTile))) - 0.5);\n" +
                    "        \n" +
                    "        // rgb shift\n" +
                    "        float texR = texture2D( u_texture, texCoord.st-vec2(u_rgbshift) ).r;\n" +
                    "        float texG = texture2D( u_texture, texCoord.st ).g;\n" +
                    "        float texB = texture2D( u_texture, texCoord.st+vec2(u_rgbshift) ).b;\n" +
                    "        \n" +
                    "        float clip = step(0.0, texCoord.s) * step(texCoord.s, 1.0) * step(0.0, texCoord.t) * step(texCoord.t, 1.0); \n" +
                    "        gl_FragColor  = vec4( vec3(texR, texG, texB) * stripFac * clip, 1.0 );\n" +
                    "    }"
            ff_ShaderInit = { init ->
                init.setUniformFloat("u_distortion", 0.1f)
                init.setUniformFloat("u_stripe", 0.1f)
                init.setUniformFloat("u_rgbshift", 0.1f)
            }
        }

        View.buildAndActivate {
            ff_Name="ShaderView"
            ff_Bounds(0,0,400,400)
            ff_Shader("Shader")
        }

        Entity.buildAndActivate {
            ff_With(ETransform) {
                ff_View("ShaderView")
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
        val config = Lwjgl3ApplicationConfiguration()
        config.setResizable(true)
        config.setWindowedMode(800, 600)
        Lwjgl3Application(ViewShaderTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}