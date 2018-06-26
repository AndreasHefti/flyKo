package com.inari.firefly.graphics

import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.FFContext
import com.inari.firefly.asset.Asset
import com.inari.firefly.external.ShaderData
import com.inari.firefly.system.component.SubType

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

    var ff_VertexShaderName: String
        get() = data.vertexShaderResourceName
        set(value) {data.vertexShaderResourceName = setIfNotInitialized(value, "ff_VertexShaderName")}
    var ff_VertexShaderProgr: String
        get() = data.vertexShaderProgram
        set(value) {data.vertexShaderProgram = setIfNotInitialized(value, "ff_VertexShaderProgr")}
    var ff_FragmentShaderName: String
        get() = data.fragmentShaderResourceName
        set(value) {data.fragmentShaderResourceName = setIfNotInitialized(value, "ff_FragmentShaderName")}
    var ff_FragmentShaderProgr: String
        get() = data.fragmentShaderProgram
        set(value) {data.fragmentShaderProgram = setIfNotInitialized(value, "ff_FragmentShaderProgr")}

    override fun load() {
        if (shaderId < 0)
            FFContext.graphics.createShader(data)
    }

    override fun unload() {
       if (shaderId >= 0) {
           FFContext.graphics.disposeShader(shaderId)
           shaderId = -1
       }
    }

    companion object : SubType<ShaderAsset, Asset>() {
        override val typeKey: IndexedTypeKey = Asset.typeKey
        override fun subType() = ShaderAsset::class.java
        override fun createEmpty() = ShaderAsset()
    }
}