package com.inari.firefly.asset

import com.inari.firefly.component.CompId
import com.inari.firefly.FFContext
import com.inari.firefly.system.FFEvent

typealias AssetEventListener = (CompId, AssetEvent.Type) -> Unit
object AssetEvent : FFEvent<AssetEventListener>(createTypeKey(AssetEvent::class.java)) {

    enum class Type {
        ASSET_CREATED,
        ASSET_LOADED,
        ASSET_DISPOSED,
        ASSET_DELETED
    }

    private lateinit var assetId: CompId
    private lateinit var type: Type

    override fun notify(listener: AssetEventListener) =
        listener(assetId, type)

    fun send(id: CompId, type: AssetEvent.Type) {
        assetId = id
        this.type = type
        FFContext.notify(this)
    }

}