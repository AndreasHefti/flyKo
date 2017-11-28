/*******************************************************************************
 * Copyright (c) 2015 - 2016, Andreas Hefti, inarisoft@yahoo.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inari.firefly.external


interface FFInput {

    val xpos: Int
    val ypos: Int

    enum class ButtonType {
        UP,
        RIGHT,
        DOWN,
        LEFT,

        ENTER,
        FIRE_1,
        FIRE_2,

        BUTTON_A,
        BUTTON_B,
        BUTTON_C,
        BUTTON_D,

        BUTTON_0,
        BUTTON_1,
        BUTTON_2,
        BUTTON_3,
        BUTTON_4,
        BUTTON_5,
        BUTTON_6,
        BUTTON_7,
        BUTTON_8,
        BUTTON_9,
        QUIT
    }

    enum class InputType {
        MOUSE_LEFT,
        MOUSE_MIDDLE,
        MOUSE_RIGHT,
        TOUCH
    }

    fun mapKeyInput(buttonType: ButtonType, keyCode: Int)

    fun mapInputType(buttonType: ButtonType, inputType: InputType)

    fun typed(buttonType: ButtonType): Boolean

    fun isPressed(buttonType: ButtonType): Boolean

}
