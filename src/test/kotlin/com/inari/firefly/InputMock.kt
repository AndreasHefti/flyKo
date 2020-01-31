package com.inari.firefly

import com.inari.firefly.external.FFInput

object InputMock : FFInput {

    override
    val xpos: Int
        get() = 0

    override
    val ypos: Int
        get() = 0

    override fun mapKeyInput(buttonType: FFInput.ButtonType, keyCode: Int) {

    }

    override fun mapInputType(buttonType: FFInput.ButtonType, inputType: FFInput.InputType) {

    }

    override fun isPressed(buttonType: FFInput.ButtonType): Boolean {
        return false
    }

    override fun typed(buttonType: FFInput.ButtonType): Boolean {
        return false
    }


}
