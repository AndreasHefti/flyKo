package com.inari.firefly.system

import com.inari.commons.lang.indexed.BaseIndexedObject
import com.inari.firefly.Call
import com.inari.firefly.Condition

class Trigger (
    val condition: Condition,
    val call: Call
) : BaseIndexedObject() {
    override fun indexedObjectType() = Trigger::class.java
}