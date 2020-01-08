package com.inari.firefly.composite.tileset

import com.inari.firefly.NO_NAME
import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.graphics.sprite.Sprite
import com.inari.firefly.physics.animation.timeline.Frame
import com.inari.util.collection.DynArray

@ComponentDSL
class AnimationData internal constructor() {

    @JvmField internal val frames:  DynArray<Frame.SpriteFrame> =
            DynArray.of(Frame.SpriteFrame::class.java, 5, 5)

    @JvmField internal val sprites: MutableMap<String, Sprite> =
            mutableMapOf()

    fun ff_addFrame(timeInterval: Long, sprite: Sprite): AnimationData {
        if (sprite.name == NO_NAME)
            throw IllegalArgumentException("Missing name")

        frames.add(Frame.SpriteFrame(sprite,  timeInterval))
        sprites[sprite.name] = sprite
        return this
    }

    fun ff_addFrame(timeInterval: Long, name: String): AnimationData {
        val sprite = sprites[name]!!
        frames.add(Frame.SpriteFrame(sprite,  timeInterval))
        return this
    }

    companion object {

        val of: (AnimationData.() -> Unit) -> AnimationData = { configure ->
            val comp = AnimationData()
            comp.also(configure)
            comp
        }

    }
}