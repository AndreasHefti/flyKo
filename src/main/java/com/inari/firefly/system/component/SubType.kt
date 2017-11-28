package com.inari.firefly.system.component

import com.inari.firefly.component.ComponentType

abstract class SubType<CC : C, C : SystemComponent> : SystemComponentBuilder<CC>(), ComponentType<C> {
    abstract fun subType(): Class<CC>
}