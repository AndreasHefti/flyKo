package com.inari.firefly.system.component

import com.inari.firefly.component.ComponentType

abstract class SubType<CC : C, C : SystemComponent> : ComponentType<C>, SystemComponentBuilder<CC>() {
    abstract val subType: Class<CC>
}