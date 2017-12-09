package com.inari.firefly.system

import com.inari.firefly.Call
import com.inari.firefly.Condition

class Trigger (
    val condition: Condition,
    val call: Call
)