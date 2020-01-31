package com.inari.firefly.graphics

import com.inari.firefly.GLBlendMode.GL_DST_ALPHA
import com.inari.firefly.GLBlendMode.GL_DST_COLOR
import com.inari.firefly.GLBlendMode.GL_ONE
import com.inari.firefly.GLBlendMode.GL_ONE_MINUS_DST_ALPHA
import com.inari.firefly.GLBlendMode.GL_ONE_MINUS_SRC_ALPHA
import com.inari.firefly.GLBlendMode.GL_SRC_ALPHA
import com.inari.firefly.GLBlendMode.GL_ZERO


enum class BlendMode constructor(var source: Int, var dest: Int) {
    /** No blending. Disables blending  */
    NONE(-1, -1),
    /** Normal alpha blending. GL11: GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA  */
    NORMAL_ALPHA(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA),
    /** Additive blending ( without alpha ). GL11: GL_ONE, GL_ONE )  */
    ADDITIVE(GL_ONE, GL_ONE),
    /** Additive blending ( with alpha ). GL11: GL_SRC_ALPHA, GL_ONE  */
    ADDITIVE_ALPHA(GL_SRC_ALPHA, GL_ONE),
    /** Multiplied blending. GL11: GL_DST_COLOR, GL_ZERO  */
    MULT(GL_DST_COLOR, GL_DST_COLOR),
    /** Clears the destination. GL11: GL_ZERO, GL_ZERO  */
    CLEAR(GL_ZERO, GL_ZERO),
    /** The source overlaps the destination. GL11: GL_ONE, GL_ZERO  */
    SRC(GL_ONE, GL_ZERO),
    /** Only the destination. GL11: GL_ZERO, GL_ONE  */
    DEST(GL_ZERO, GL_ZERO),
    SRC_OVER_DEST(GL_ONE, GL_ONE_MINUS_SRC_ALPHA),
    DEST_OVER_SRC(GL_ONE_MINUS_DST_ALPHA, GL_ONE),
    SRC_IN_DEST(GL_DST_ALPHA, GL_ZERO),
    DEST_IN_SRC(GL_ONE, GL_SRC_ALPHA),
    SRC_OUT_DEST(GL_ONE_MINUS_DST_ALPHA, GL_ZERO),
    DEST_OUT_SRC(GL_ZERO, GL_ONE_MINUS_SRC_ALPHA),
    SRC_ATOP_DEST(GL_DST_ALPHA, GL_ONE_MINUS_SRC_ALPHA),
    DEST_ATOP_SRC(GL_ONE_MINUS_DST_ALPHA, GL_DST_ALPHA),
    SRC_XOR_DEST(GL_ONE_MINUS_DST_ALPHA, GL_ONE_MINUS_SRC_ALPHA),
}
