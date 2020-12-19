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

    @JvmField internal var activeForLayer = BitSet()
    @JvmField internal var textureRef: Int = -1
    @JvmField internal val tiles: DynArray<ProtoTile> = DynArray.of()

    val textureAsset = ComponentRefResolver(Asset) { index->
            textureRef = setIfNotInitialized(index, "TextureAsset")
        }

    val tile: (ProtoTile.() -> Unit) -> Unit = { configure ->
        val tile = ProtoTile()
        tile.also(configure)
        tiles.add(tile)
    }
    var activationResolver: (TileSet) -> TileSetActivation  = { _ -> TileSetActivation() }

    operator fun contains(layer: Layer) = activeForLayer.get(layer.index)

    override fun load() {
        if (loaded)
            return

        SpriteSetAsset.build {
            name = this@TileSet.name
            texture(this@TileSet.textureRef)

            this@TileSet.tiles.forEach{
                if (it.int_animation != null)
                    spriteData.addAll(it.int_animation!!.sprites.values)
                spriteData.add(it.spriteData)
            }
        }

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
                component(ETransform) {
                    view(activation.viewRef)
                    layer(layer)
                }
                component(ETile) {
                    sprite.instanceId = spriteId
                    tint = tile.tintColor ?: activation.tintColors[layer.index] ?: tint
                    blend = tile.blendMode ?: activation.blendModes[layer.index] ?: blend
                }

                if (tile.hasContactComp) {
                    component(EContact) {
                        if (tile.contactType !== ContactSystem.UNDEFINED_CONTACT_TYPE) {
                            bounds(0,0,
                                    tile.spriteData.textureBounds.width,
                                    tile.spriteData.textureBounds.height)
                            contactType = tile.contactType
                            material = tile.material
                            mask = tile.contactMask ?: mask
                        }
                        material = tile.material
                    }
                }

                if (tile.int_animation != null) {
                    component(EAnimation) {
                        activeAnimation(IntTimelineProperty) {
                            looping = true
                            timeline = tile.int_animation!!.frames.toArray()
                            propertyRef = ETile.Property.SPRITE_REFERENCE
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
    }

    companion object : SystemComponentSubType<Composite, TileSet>(Composite, TileSet::class.java) {
        override fun createEmpty() = TileSet()
    }
}