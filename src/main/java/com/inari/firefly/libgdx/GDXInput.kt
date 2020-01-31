package com.inari.firefly.libgdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Buttons.*
import com.inari.firefly.external.FFInput
import com.inari.firefly.external.FFInput.InputType.MOUSE_LEFT
import com.inari.firefly.external.FFInput.InputType.MOUSE_RIGHT
import com.inari.firefly.external.FFInput.InputType.MOUSE_MIDDLE
import com.inari.java.types.BitSet
import com.inari.util.collection.DynArray
import com.inari.util.collection.DynIntArray
import net.java.games.input.Component.Identifier.Button.TOUCH

object GDXInput : FFInput {

    private val buttonCodeMapping = DynIntArray(255, -1)
    private val pressedCodeMapping = BitSet(255)
    private val inputTypeMapping: DynArray<FFInput.InputType> = DynArray.of()

    override val xpos: Int
        get() = Gdx.input.x
    override val ypos: Int
        get() = Gdx.input.y

    override fun isPressed(buttonType: FFInput.ButtonType): Boolean {
        val buttonCode = buttonType.ordinal
        val keyCode = buttonCodeMapping[buttonCode]
        if (keyCode >= 0 && Gdx.input.isKeyPressed(keyCode))
            return true

        var pressed = false
        if (buttonCode in inputTypeMapping) {
            when (inputTypeMapping[buttonCode]) {
                MOUSE_LEFT      -> pressed = Gdx.input.isButtonPressed(LEFT)
                MOUSE_RIGHT     -> pressed = Gdx.input.isButtonPressed(RIGHT)
                MOUSE_MIDDLE    -> pressed = Gdx.input.isButtonPressed(MIDDLE)
                TOUCH           -> pressed = Gdx.input.isTouched
                else            -> {}
            }
        }
        return pressed
    }

    override fun typed(buttonType: FFInput.ButtonType): Boolean {
        val buttonCode = buttonType.ordinal
        val keyCode = buttonCodeMapping[buttonCode]
        val pressed = isPressed(buttonType)

        if (pressed && pressedCodeMapping[keyCode])
            return false

        pressedCodeMapping.set(keyCode, pressed)
        return pressed
    }

    override fun mapInputType(buttonType: FFInput.ButtonType, inputType: FFInput.InputType) {
        inputTypeMapping[buttonType.ordinal] = inputType
    }

    override fun mapKeyInput(buttonType: FFInput.ButtonType, keyCode: Int) {
        buttonCodeMapping[buttonType.ordinal] = keyCode
    }
}