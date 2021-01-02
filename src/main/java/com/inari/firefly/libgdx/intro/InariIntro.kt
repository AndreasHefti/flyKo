package com.inari.firefly.libgdx.intro

import com.inari.firefly.*
import com.inari.firefly.entity.Entity
import com.inari.firefly.external.FFInput
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.TextureAsset
import com.inari.firefly.graphics.sprite.ESprite
import com.inari.firefly.graphics.sprite.SpriteAsset
import com.inari.firefly.libgdx.DesktopInput
import com.inari.firefly.physics.animation.easing.EasedProperty
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.util.Call
import com.inari.util.geom.Easing.Type
import com.inari.util.geom.PositionF
import com.inari.util.geom.Rectangle
import com.inari.util.graphics.RGBColor
import org.lwjgl.glfw.GLFW

object InariIntro {

    private var textureAssetId = NO_COMP_ID
    private var spriteAssetId = NO_COMP_ID
    private var entityId = NO_COMP_ID
    private var animationId = NO_COMP_ID
    private var callback: Call = VOID_CALL
    private var disposing = false

    internal fun show(callback: Call) {
        this.callback = callback

        val texture: TextureAsset = TextureAsset.buildActivateAndGet {
            resourceName = "firefly/inari.png"
        }
        textureAssetId = texture.componentId

        spriteAssetId = SpriteAsset.buildAndActivate {
            texture(textureAssetId)
            textureRegion = Rectangle( 0, 0, texture.width, texture.height )
        }

        entityId = Entity.buildAndActivate {
            component(ETransform) {
                view(BASE_VIEW)
                position(PositionF(
                    FFContext.screenWidth / 2 - texture.width / 2,
                    FFContext.screenHeight / 2 - texture.height / 2
                ))
            }

            component(ESprite) {
                sprite(spriteAssetId)
                tint(RGBColor(1f, 1f, 1f, 0f))
            }

            component(EAnimation) {
                animationId = activeAnimation(EasedProperty) {
                    easing = Type.LINEAR
                    startValue = 0f
                    endValue = 1f
                    duration = 1000
                    propertyRef = ESprite.Property.TINT_ALPHA
                    resetOnFinish = false
                }
            }
        }

        FFContext.input.setKeyCallback { _, _, _ -> dispose() }
        FFContext.input.setMouseButtonCallback { _, _ -> dispose() }
        val controllerInput = FFContext.input.createDevice<DesktopInput.GLFWControllerInput>(
                "ControllerInput",
                DesktopInput.GLFWControllerInput)
        controllerInput.mapButtonInput(FFInput.ButtonType.BUTTON_A, GLFW.GLFW_GAMEPAD_BUTTON_A)
        controllerInput.mapButtonInput(FFInput.ButtonType.BUTTON_B, GLFW.GLFW_GAMEPAD_BUTTON_B)
        controllerInput.mapButtonInput(FFInput.ButtonType.BUTTON_X, GLFW.GLFW_GAMEPAD_BUTTON_X)
        controllerInput.mapButtonInput(FFInput.ButtonType.BUTTON_Y, GLFW.GLFW_GAMEPAD_BUTTON_Y)
        controllerInput.slot = 0
        FFContext.input.setButtonCallback("ControllerInput") { _, _ -> dispose() }
    }

    private fun dispose() {
        if (!disposing) {
            disposing = true
            FFContext.deleteQuietly(entityId)
            FFContext.deleteQuietly(spriteAssetId)
            FFContext.deleteQuietly(textureAssetId)
            FFContext.deleteQuietly(animationId)
            FFContext.input.resetInputCallbacks()
            callback()
        }
    }
}