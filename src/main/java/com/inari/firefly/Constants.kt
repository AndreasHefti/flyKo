package com.inari.firefly

import com.inari.firefly.component.CompId


@JvmField val NO_NAME: String = "[[NO_NAME]]"
@JvmField val NO_COMP_ID: CompId = CompId(-1, null!!)

@JvmField val BASE_VIEW: String = "[[BASE_VIEW]]"


object GLBlendMode {
    val GL_ZERO = 0x0
    val GL_ONE = 0x1
    val GL_SRC_COLOR = 0x300
    val GL_ONE_MINUS_SRC_COLOR = 0x301
    val GL_SRC_ALPHA = 0x302
    val GL_ONE_MINUS_SRC_ALPHA = 0x303
    val GL_DST_ALPHA = 0x304
    val GL_ONE_MINUS_DST_ALPHA = 0x305
    val GL_DST_COLOR = 0x306
    val GL_ONE_MINUS_DST_COLOR = 0x307
    val GL_SRC_ALPHA_SATURATE = 0x308
    val GL_CONSTANT_COLOR = 0x8001
    val GL_ONE_MINUS_CONSTANT_COLOR = 0x8002
    val GL_CONSTANT_ALPHA = 0x8003
    val GL_ONE_MINUS_CONSTANT_ALPHA = 0x8004
}
