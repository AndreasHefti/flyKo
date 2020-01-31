package com.inari.firefly

interface Named {
    val name: String
}

enum class OpResult {
    SUCCESS,
    RUNNING,
    FAILED
}


