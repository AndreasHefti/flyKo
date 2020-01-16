package com.inari.firefly.libgdx.intro

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.inari.firefly.*
import com.inari.firefly.entity.Entity
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.TextureAsset
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.graphics.sprite.SpriteAsset
import com.inari.firefly.physics.animation.easing.EasedProperty
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.util.geom.Easing
import com.inari.util.geom.PositionF
import com.inari.util.geom.Rectangle
import com.inari.util.graphics.RGBColor

object InariIntro {

    private var textureAssetId = NO_COMP_ID
    private var spriteAssetId = NO_COMP_ID
    private var entityId = NO_COMP_ID

    private var updateListener = object : FFApp.UpdateEvent.Listener {
        override fun invoke() {
            if ( Gdx.input.isKeyPressed( Input.Keys.SPACE ) ||
                Gdx.input.isTouched ||
                Gdx.input.isButtonPressed( Input.Buttons.LEFT ) )
                dispose()
        }
    }
    private var callback: Call = VOID_CALL

    internal fun show(callback: Call) {
        this.callback = callback

        val texture: TextureAsset = TextureAsset.buildActivateAndGet {
            ff_ResourceName = "firefly/inari.png"
        }
        textureAssetId = texture.componentId

        spriteAssetId = SpriteAsset.buildAndActivate {
            ff_Texture(textureAssetId)
            ff_TextureRegion = Rectangle( 0, 0, texture.width, texture.height )
        }

        entityId = Entity.buildAndActivate {
            withComponent(ETransform) {
                ff_View(0)
                ff_Position(PositionF(
                    FFContext.screenWidth / 2 - texture.width / 2,
                    FFContext.screenHeight / 2 - texture.height / 2
                ))
            }

            withComponent(ESprite) {
                ff_Sprite(spriteAssetId)
                ff_Tint(RGBColor(1f, 1f, 1f, 0f))
            }

            withComponent(EAnimation) {
                withActiveAnimation(EasedProperty) {
                    ff_Easing = Easing.Type.LINEAR
                    ff_StartValue = 0f
                    ff_EndValue = 1f
                    ff_Duration = 1000
                    ff_PropertyRef = ESprite.Property.TINT_ALPHA
                }
            }
        }

        FFContext.registerListener(FFApp.UpdateEvent, updateListener)
    }

    private fun dispose() {
        FFContext.disposeListener(FFApp.UpdateEvent, updateListener)
        FFContext.delete(entityId)
        FFContext.delete(spriteAssetId)
        FFContext.delete(textureAssetId)

        callback()
    }
}