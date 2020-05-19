package com.inari.firefly.libgdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Buttons.*
import com.badlogic.gdx.InputProcessor
import com.inari.firefly.VOID_CALL
import com.inari.firefly.VOID_INT_CONSUMER
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
    private var inputListener = 0
    private var iKeyDownSet = false
    private var iKeyDown: (Int) -> Unit = {}
    private var iKeyTypedSet = false
    private var iKeyTyped: (Char) -> Unit = {}
    private var iKeyUpSet = false
    private var iKeyUp: (Int) -> Unit = {}
    private val inputProcessor: InputProcessor = object : InputProcessor {

        override fun keyTyped(character: Char): Boolean {
            iKeyTyped.invoke(character)
            return false
        }

        override fun keyUp(keycode: Int): Boolean {
            iKeyUp.invoke(keycode)
            return false
        }

        override fun keyDown(keycode: Int): Boolean {
            iKeyDown.invoke(keycode)
            return false
        }

        override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
            TODO("Not yet implemented")
        }

        override fun scrolled(amount: Int): Boolean {
            TODO("Not yet implemented")
        }

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
            TODO("Not yet implemented")
        }

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            TODO("Not yet implemented")
        }

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            TODO("Not yet implemented")
        }
    }


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

    override fun listenKeyDown(keyDown: (Int) -> Unit) {
        Gdx.input.inputProcessor = inputProcessor
        iKeyDown = keyDown
        if (!iKeyDownSet)
            inputListener++
        iKeyDownSet = true
    }

    override fun stopListenKeyDown() {
        iKeyDown = {}
        if (iKeyDownSet)
            inputListener--
        if (inputListener <= 0)
            Gdx.input.inputProcessor = null
    }

    override fun listenKeyTyped(keyTyped: (Char) -> Unit) {
        Gdx.input.inputProcessor = inputProcessor
        iKeyTyped = keyTyped
        if (!iKeyTypedSet)
            inputListener++
        iKeyTypedSet = true
    }

    override fun stopListenKeyTyped() {
        iKeyTyped = {}
        if (iKeyTypedSet)
            inputListener--
        if (inputListener <= 0)
            Gdx.input.inputProcessor = null
    }

    override fun listenKeyUp(keyUp: (Int) -> Unit) {
        Gdx.input.inputProcessor = inputProcessor
        iKeyUp = keyUp
        if (!iKeyUpSet)
            inputListener++
        iKeyUpSet = true
    }

    override fun stopListenKeyUp() {
        iKeyUp = {}
        if (iKeyUpSet)
            inputListener--
        if (inputListener <= 0)
            Gdx.input.inputProcessor = null
    }

}