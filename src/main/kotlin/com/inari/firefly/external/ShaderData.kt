package com.inari.firefly.external

import com.inari.firefly.NO_NAME
import com.inari.firefly.NO_PROGRAM

class ShaderData(
    @JvmField var name: String = NO_NAME,
    @JvmField var vertexShaderResourceName: String = NO_NAME,
    @JvmField var vertexShaderProgram: String = NO_PROGRAM,
    @JvmField var fragmentShaderResourceName: String = NO_NAME,
    @JvmField var fragmentShaderProgram: String = NO_PROGRAM
) {

    fun reset() {
        name = NO_NAME
        vertexShaderResourceName = NO_NAME
        vertexShaderProgram = NO_PROGRAM
        fragmentShaderResourceName = NO_NAME
        fragmentShaderProgram = NO_PROGRAM
    }
}