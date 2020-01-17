package com.inari.firefly.graphics.tile.set

import com.inari.firefly.NO_NAME
import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.graphics.sprite.ProtoSprite
import com.inari.firefly.physics.animation.timeline.Frame
import com.inari.util.collection.DynArray

@ComponentDSL
class TileAnimation internal constructor() {

    @JvmField internal val frames:  DynArray<Frame.SpriteFrame> =
            DynArray.of(Frame.SpriteFrame::class.java, 5, 5)

    @JvmField internal val sprites: MutableMap<String, ProtoSprite> =
            mutableMapOf()

    val ff_withFrame: (Frame.SpriteFrame.() -> Unit) -> Unit = { configure ->
        val frame = Frame.SpriteFrame()
        frame.also(configure)

        if (frame.sprite.name == NO_NAME)
                throw IllegalArgumentException("Missing name")

        frames.add(frame)
        sprites[frame.sprite.name] = frame.sprite
    }

//    fun ff_addFrame(timeInterval: Long, sprite: ProtoSprite): AnimationData {
//        if (sprite.name == NO_NAME)
//            throw IllegalArgumentException("Missing name")
//
//        frames.add(Frame.SpriteFrame(sprite,  timeInterval))
//        sprites[sprite.name] = sprite
//        return this
//    }
//
//    fun ff_addFrame(timeInterval: Long, name: String): AnimationData {
//        val sprite = sprites[name]!!
//        frames.add(Frame.SpriteFrame(sprite,  timeInterval))
//        return this
//    }

    companion object {

        val of: (TileAnimation.() -> Unit) -> TileAnimation = { configure ->
            val comp = TileAnimation()
            comp.also(configure)
            comp
        }

    }
}