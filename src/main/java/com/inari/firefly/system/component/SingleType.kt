package com.inari.firefly.system.component

import com.inari.firefly.component.ComponentType

abstract class SingleType<C : SystemComponent> : ComponentType<C>, SystemComponentBuilder<C>()