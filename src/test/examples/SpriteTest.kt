import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.entity.Entity
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.TextureAsset
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.graphics.sprite.SpriteAsset
import com.inari.firefly.libgdx.DesktopAppAdapter

class SpriteTest : DesktopAppAdapter() {

    override val title: String = "IntroTest"

    override fun init() {

        TextureAsset.build {
            name = "TEST"
            resourceName = "firefly/inari.png"
        }

        SpriteAsset.buildAndActivate {
            name = "TestSprite"
            texture("TEST")
            textureRegion(0,0,100,100)
        }

        Entity.buildAndActivate {
            component(ETransform) {
                position(0,0)
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
        Lwjgl3Application(SpriteTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}