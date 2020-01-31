package com.inari.java.types

typealias ArrayDeque<T> = java.util.ArrayDeque<T>
typealias StringTokenizer = java.util.StringTokenizer
typealias BitSet = java.util.BitSet
operator fun BitSet.contains(index: Int): Boolean = this.get(index)