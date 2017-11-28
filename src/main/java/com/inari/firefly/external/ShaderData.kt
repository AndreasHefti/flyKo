package com.inari.firefly.external

interface ShaderData {
    val name: String
    val vertexShaderResourceName: String
    val vertexShaderProgram: String
    val fragmentShaderResourceName: String
    val fragmentShaderProgram: String
}