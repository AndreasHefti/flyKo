package com.inari.util

import java.util.*

operator fun BitSet.contains(index: Int): Boolean = this.get(index)