package com.inari.firefly.system.component

import com.inari.firefly.component.ComponentType

interface ISubType<CC : C, C : SystemComponent> : ComponentType<C> {
    abstract fun subType(): Class<CC>
}