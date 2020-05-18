package com.inari.firefly.graphics

import com.inari.firefly.FFContext
import com.inari.firefly.asset.Asset
import com.inari.firefly.external.ShaderData
import com.inari.firefly.external.ShaderInit
import com.inari.firefly.system.component.SystemComponentSubType

class ShaderAsset private constructor() : Asset() {

    @JvmField internal var shaderId: Int = -1
    override fun instanceId(index: Int): Int = shaderId

    private val data = ShaderData()

    override var name
        get() = data.name
        set(value) {
            super.name = value
            data.name = value
        }

    var ff_VertShaderResource: String
        get() = data.vertexShaderResourceName
        set(value) {data.vertexShaderResourceName = setIfNotInitialized(value, "ff_VertShaderResource")}
    var ff_VertShaderProgram: String
        get() = data.vertexShaderProgram
        set(value) {data.vertexShaderProgram = setIfNotInitialized(value, "ff_VertShaderProgram")}
    var ff_FragShaderResource: String
        get() = data.fragmentShaderResourceName
        set(value) {data.fragmentShaderResourceName = setIfNotInitialized(value, "ff_FragShaderResource")}
    var ff_FragShaderProgram: String
        get() = data.fragmentShaderProgram
        set(value) {data.fragmentShaderProgram = setIfNotInitialized(value, "ff_FragShaderProgram")}
    var ff_ShaderInit: ShaderInit
        get() = data.shaderInit
        set(value) {data.shaderInit = setIfNotInitialized(value, "ff_ShaderInit")}

    override fun load() {
        if (shaderId < 0)
            shaderId = FFContext.graphics.createShader(data)
    }

    override fun unload() {
       if (shaderId >= 0) {
           FFContext.graphics.disposeShader(shaderId)
           shaderId = -1
       }
    }

    companion object : SystemComponentSubType<Asset, ShaderAsset>(Asset, ShaderAsset::class.java) {
        override fun createEmpty() = ShaderAsset()
    }
}