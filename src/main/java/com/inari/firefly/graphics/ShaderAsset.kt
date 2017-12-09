package com.inari.firefly.graphics

import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.FFContext
import com.inari.firefly.NO_NAME
import com.inari.firefly.NO_PROGRAM
import com.inari.firefly.asset.Asset
import com.inari.firefly.external.ShaderData
import com.inari.firefly.system.component.SubType

class ShaderAsset private constructor(

) : Asset(), ShaderData {

    @JvmField internal var shaderId: Int = -1
    override fun instanceId(index: Int): Int = shaderId

    override val name: String
        get() = name()
    override var vertexShaderResourceName: String = NO_NAME
        private set
    override var vertexShaderProgram: String = NO_PROGRAM
        private set
    override var fragmentShaderResourceName: String = NO_NAME
        private set
    override var fragmentShaderProgram: String = NO_PROGRAM
        private set

    var ff_VertexShaderName: String
        get() = vertexShaderResourceName
        set(value) {vertexShaderResourceName = setIfNotInitialized(value, "ff_VertexShaderName")}
    var ff_VertexShaderProgr: String
        get() = vertexShaderProgram
        set(value) {vertexShaderProgram = setIfNotInitialized(value, "ff_VertexShaderProgr")}
    var ff_FragmentShaderName: String
        get() = fragmentShaderResourceName
        set(value) {fragmentShaderResourceName = setIfNotInitialized(value, "ff_FragmentShaderName")}
    var ff_FragmentShaderProgr: String
        get() = fragmentShaderProgram
        set(value) {fragmentShaderProgram = setIfNotInitialized(value, "ff_FragmentShaderProgr")}

    override fun load() {
        if (shaderId < 0)
            FFContext.graphics.createShader(this)
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