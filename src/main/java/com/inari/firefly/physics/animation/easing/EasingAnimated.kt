package com.inari.firefly.physics.animation.easing

import com.inari.commons.geom.Easing
import com.inari.firefly.physics.animation.AnimatedProperty

abstract class EasingAnimated private constructor() : AnimatedProperty() {

    protected var easingType: Easing.Type = Easing.Type.LINEAR
}