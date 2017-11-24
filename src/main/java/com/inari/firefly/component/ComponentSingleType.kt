package com.inari.firefly.component

interface ComponentSingleType<C : Component> : ComponentSubType<C, C> {
    override fun subType(): Class<C> = type()
}