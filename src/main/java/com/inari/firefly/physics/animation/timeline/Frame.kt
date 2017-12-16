package com.inari.firefly.physics.animation.timeline

import com.inari.firefly.graphics.sprite.Sprite

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

    class SpriteFrame(
        val sprite: Sprite,
        override val timeInterval: Long
    ) : IntFrame {
        override val value: Int
            get() = sprite.instanceId
    }
}