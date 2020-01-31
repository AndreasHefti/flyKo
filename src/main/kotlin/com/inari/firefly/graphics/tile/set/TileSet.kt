package com.inari.firefly.graphics.tile.set

import com.inari.firefly.FFContext
import com.inari.firefly.asset.Asset
import com.inari.firefly.asset.AssetSystem
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.composite.Composite
import com.inari.firefly.entity.Entity
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.sprite.SpriteSetAsset
import com.inari.firefly.graphics.tile.ETile
import com.inari.firefly.graphics.view.Layer
import com.inari.firefly.graphics.view.ViewSystem
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.firefly.physics.animation.timeline.IntTimelineProperty
import com.inari.firefly.physics.contact.ContactSystem
import com.inari.firefly.physics.contact.EContact
import com.inari.firefly.system.component.SystemComponentSubType
import com.inari.java.types.BitSet
import com.inari.util.collection.DynArray

class TileSet private constructor() : Composite() {

    @JvmField internal var textureRef: Int = -1
    @JvmField internal val tiles: DynArray<ProtoTile> = DynArray.of()
    @JvmField internal var activationResolver: (TileSet) -> TileSetActivation = { _ -> TileSetActivation() }

    val ff_TextureAsset = ComponentRefResolver(Asset) { index->
            textureRef = setIfNotInitialized(index, "ff_TextureAsset")
        }

    val ff_withTile: (ProtoTile.() -> Unit) -> Unit = { configure ->
        val tile = ProtoTile()
        tile.also(configure)
        tiles.add(tile)
    }

    var ff_ActivationResolver: (TileSet) -> TileSetActivation
        set(value) {activationResolver = value}
        get() = activationResolver

    @JvmField internal var activeForLayer = BitSet()

    operator fun contains(layer: Layer) = activeForLayer.get(layer.index)

    override fun load() {
        if (loaded)
            return

        SpriteSetAsset.build {
            ff_Name = this@TileSet.name
            ff_Texture(this@TileSet.textureRef)

            this@TileSet.tiles.forEach{
                if (it.animation != null)
                    ff_SpriteData.addAll(it.animation!!.sprites.values)
                ff_SpriteData.add(it.spriteData)
            }
        }

        loaded = true
    }

    override fun activate() {
        if (!loaded)
            load()

        if (name !in AssetSystem.assets)
            throw IllegalStateException()

        AssetSystem.assets.activate(name)
        val activation = activationResolver.invoke(this)

        ViewSystem.layers.forEach { layer ->
            if (layer in activation && layer !in this && activate(layer, activation))
                activeForLayer.set(layer.index)
        }

        TileSetSystem.activated(this)
    }

    operator fun get(tileIndex: Int, layer: Layer): Int =
            get(tileIndex, layer.index)

    operator fun get(tileIndex: Int, layerId: Int): Int =
            if (tileIndex !in tiles) -1
            else tiles[tileIndex]?.entityIds?.get(layerId) ?: -1


    private fun activate(layer: Layer, activation: TileSetActivation): Boolean {
        if (layer in this)
            return false

        var it = 0
        while (it < tiles.capacity) {
            val tile = tiles[it++] ?: continue

            if (tile === ProtoTile.EMPTY_TILE)
                continue

            val spriteId = tile.spriteData.instanceId
            if (spriteId < 0)
                return false

            tile.entityIds[layer.index] = Entity.buildAndActivate {
                ff_With(ETransform) {
                    ff_View(activation.viewRef)
                    ff_Layer(layer)
                }
                ff_With(ETile) {
                    ff_Sprite.instanceId = spriteId
                    ff_Tint = tile.tintColor ?: activation.tintColors[layer.index] ?: ff_Tint
                    ff_Blend = tile.blendMode ?: activation.blendModes[layer.index] ?: ff_Blend
                }

                if (tile.hasContactComp) {
                    ff_With(EContact) {
                        if (tile.contactType !== ContactSystem.UNDEFINED_CONTACT_TYPE) {
                            ff_Bounds(
                                    0,
                                    0,
                                    tile.spriteData.textureBounds.width,
                                    tile.spriteData.textureBounds.height)
                            ff_ContactType = tile.contactType
                            ff_Material = tile.material
                            ff_Mask = tile.contactMask ?: ff_Mask
                        }
                        ff_Material = tile.material
                    }
                }

                if (tile.animation != null) {
                    ff_With(EAnimation) {
                        withActiveAnimation(IntTimelineProperty) {
                            ff_Looping = true
                            ff_Timeline = tile.animation!!.frames.toArray()
                            ff_PropertyRef = ETile.Property.SPRITE_REFERENCE
                        }
                    }
                }
            }.instanceId
        }
        return true
    }

    override fun deactivate() {
        if (activeForLayer.isEmpty)
            return

        var ii = activeForLayer.nextSetBit(0)
        while(ii >= 0) {
            deactivate(this, ii)
            ii = activeForLayer.nextSetBit(ii + 1)
        }

        FFContext.deactivate(Asset, name)
        TileSetSystem.deactivated(this)
    }

    private fun deactivate(tileSet: TileSet, layerId: Int) {
        var i = 0
        while (i < tileSet.tiles.capacity) {
            val tile = tileSet.tiles[i++] ?: continue

            if (tile === ProtoTile.EMPTY_TILE)
                continue

            val entityId = tile.entityIds[layerId]
            if (entityId < 0)
                continue

            FFContext.delete(Entity, entityId)
            tile.entityIds[layerId] = -1
        }

        tileSet.activeForLayer[layerId] = false
    }

    override fun unload() {
        if (!loaded)
            return

        FFContext.deactivate(TileSet, name)
        FFContext.delete(Asset, name)

        loaded = false
    }

    companion object : SystemComponentSubType<Composite, TileSet>(Composite, TileSet::class.java) {
        override fun createEmpty() = TileSet()
    }
}