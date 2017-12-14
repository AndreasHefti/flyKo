package com.inari.firefly.system.component

abstract class SubType<CC : C, C : SystemComponent> : ISubType<CC, C>, SystemComponentBuilder<CC>()