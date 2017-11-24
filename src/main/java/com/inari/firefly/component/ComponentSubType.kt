package com.inari.firefly.component

interface ComponentSubType<CC : C, C : Component> : ComponentType<C> {
    fun subType(): Class<CC>
}