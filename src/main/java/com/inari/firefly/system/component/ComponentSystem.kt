package com.inari.firefly.system.component

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.system.FFSystem

interface ComponentSystem : FFSystem {
    val supportedComponents: IAspects
}