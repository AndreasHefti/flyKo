import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.BASE_VIEW
import com.inari.firefly.entity.Entity
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.TextureAsset
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.graphics.sprite.SpriteAsset
import com.inari.firefly.libgdx.DesktopAppAdapter
import com.inari.firefly.physics.animation.easing.EasedProperty
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.util.geom.Easing

class Example1 : DesktopAppAdapter() {

    override val title: String = "Example1"

    override fun init() {
        // Create a TextureAsset and register it to the AssetSystem but no loading yet.
        //
        // Within this method you are able to define all your assets on one place without
        // loading the assets into memory yet. While when you need them you can simple load
        // them by calling FFContext.activate(TextureAsset, "logoTexture") and dispose them
        // with FFContext.dispose(TextureAsset, "logoTexture"). The asset definition is still
        // available and can be deleted with FFContext.delete(TextureAsset, "logoTexture")
        val texAssetId = TextureAsset.build {
            ff_Name = "logoTexture"
            ff_ResourceName = "firefly/logo.png"
        }

        // Create and activate/load a SpriteAsset with reference to the TextureAsset.
        // This also implicitly loads the TextureAsset if it is not already loaded.
        val spriteId = SpriteAsset.buildAndActivate {
            ff_Texture(texAssetId)
            ff_TextureRegion(0,0,32,32)
            ff_HorizontalFlip = false
            ff_VerticalFlip = false
        }

        // Create an Entity positioned on the base View on x=100/y=100, and the formerly
        // created sprite with a tint color that has animated alpha value
        Entity.buildAndActivate {

            // add a transform component to the entity that defines the orientation of the Entity
            ff_With(ETransform) {
                ff_View(BASE_VIEW)
                ff_Position(50, 150)
                ff_Scale(4f, 4f)
            }

            // add a sprite component to the entity
            ff_With(ESprite) {
                ff_Sprite(spriteId)
                ff_Tint(1f, 1f, 1f, .5f)
            }

            // add an animation component to the entity that defines a value based animation
            // and is bound to the value of the alpha value of the sprites tint color property.
            //
            // Animations normally can work for itself and live in the AnimationSystem. But if
            // a property of an Entity-Component like ESprite defines a property value adapter,
            // an animation can be bound to this property to affecting the value of the property directly.
            ff_With(EAnimation) {

                // with an active easing animation on the sprite alpha blending value...
                ff_WithActiveAnimation(EasedProperty) {
                    ff_Easing = Easing.Type.LINEAR
                    ff_StartValue = 0f
                    ff_EndValue = 1f
                    ff_Duration = 3000
                    ff_Looping = true
                    ff_InverseOnLoop = true

                    // that is connected to the alpha value of the sprite of the entity
                    ff_PropertyRef = ESprite.Property.TINT_ALPHA
                }

                // and with an active easing animation on the sprites position on the x axis...
                ff_WithActiveAnimation(EasedProperty) {
                    ff_Easing = Easing.Type.BACK_OUT
                    ff_StartValue = 50f
                    ff_EndValue = 400f
                    ff_Duration = 1000
                    ff_Looping = true
                    ff_InverseOnLoop = true

                    // that is connected to the position value on the x axis of the entities transform data
                    ff_PropertyRef = ETransform.Property.POSITION_X
                }
            }
        }
    }

}

fun main() {
    try {
        val config = Lwjgl3ApplicationConfiguration()
        config.setResizable(true)
        config.setWindowedMode(600, 400)
        Lwjgl3Application(Example1(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}