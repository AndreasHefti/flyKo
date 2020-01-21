package com.inari.firefly.physics.animation.timeline

import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.graphics.sprite.ProtoSprite

interface Frame {

    val timeInterval: Long

    interface IntFrame : Frame {
        val value: Int
    }
    interface FloatFrame : Frame {
        val value: Float
    }
    interface ValueFrame<out T> : Frame {
        val value: T
    }

    @ComponentDSL
    class SpriteFrame : IntFrame {

        @JvmField internal var sprite: ProtoSprite = ProtoSprite()
        @JvmField internal var interval: Long = 0

        var ff_Interval: Long
                get() = interval
                set(value) { interval = value }

        var ff_Sprite: ProtoSprite
            get() = sprite
            set(value) { sprite = value }

        val ff_withSprite: (ProtoSprite.() -> Unit) -> Unit = { configure ->
            val sprite = ProtoSprite()
            sprite.also(configure)
            this.sprite = sprite
        }

        override val timeInterval: Long
            get() { return interval }

        override val value: Int
            get() = sprite.instanceId

        companion object {
            val of: (SpriteFrame.() -> Unit) -> SpriteFrame = { configure ->
                val instance = SpriteFrame()
                instance.also(configure)
                instance
            }
        }
    }
}