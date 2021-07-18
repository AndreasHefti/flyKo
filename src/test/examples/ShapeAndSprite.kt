import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.external.ShapeType
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.TextureAsset
import com.inari.firefly.graphics.rendering.RenderingSystem
import com.inari.firefly.graphics.shape.EShape
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.graphics.sprite.SpriteAsset
import com.inari.firefly.graphics.view.View
import com.inari.firefly.libgdx.DesktopAppAdapter
import com.inari.firefly.physics.animation.AnimationSystem
import com.inari.util.graphics.RGBColor

class ShapeAndSprite : DesktopAppAdapter()  {

    override val title: String = "ShapeAndSprite"

    override fun init() {
        RenderingSystem
        FFContext.loadSystem(EntitySystem)
        FFContext.loadSystem(AnimationSystem)

        val id = View.buildAndActivate {
            name = "TestView"
            bounds(0, 0, 800, 600)
            blendMode = BlendMode.NORMAL_ALPHA
        }

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
                view(id)
                position(0, 0)
            }
            component(EShape) {
                shapeType = ShapeType.TRIANGLE
                color = RGBColor.RED
                fill = true
                vertices = floatArrayOf(5f, 5f, 120f, 5f, 50f, 50f)
            }
        }

        Entity.buildAndActivate {
            component(ETransform) {
                view(id)
                position(0, 0)
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
        Lwjgl3Application(ShapeAndSprite(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}