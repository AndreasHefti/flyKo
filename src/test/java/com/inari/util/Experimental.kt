package com.inari.util

typealias FloatSetter = (Float) -> Unit

interface ISetter<T> {
    fun set(value: T)
}

interface IFloatSetter : ISetter<Float> {
    override fun set(f: Float)
}

class FloatSetterImpl1 : FloatSetter {
    override fun invoke(p1: Float) {
        println(p1)
    }

}

class FloatSetterImpl : IFloatSetter {
    val float1: Float = 0.0f
    val float2: java.lang.Float = java.lang.Float(1f)


    override fun set(f: Float) {
        println(f)
    }

    fun set(setter: IFloatSetter) {
        setter.set(1.0f)
    }

    fun set(setter: ISetter<Float>) {
        val s = setter as IFloatSetter
        setter.set(1.0f)
        s.set(1.0f)
    }

    fun set(setter: FloatSetter) {
        setter.invoke(1.0f)
    }

    fun print() {
        set{f: Float -> println(f)}
    }
}