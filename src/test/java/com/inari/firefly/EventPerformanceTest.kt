package com.inari.firefly

import com.inari.commons.geom.Position
import com.inari.firefly.asset.AssetEvent
import com.inari.firefly.component.CompId
import com.inari.firefly.system.FFEvent
import javafx.geometry.Pos
import org.junit.Before
import org.junit.Test

typealias TestListenerType = (Int, Int, Int) -> Unit

interface TestListenerInterface {
    operator fun invoke(i1: Int, i2: Int, i3: Int)
}

typealias TestListenerTypeObj = (Position, Position, Position) -> Unit

interface TestListenerInterfaceObj {
    operator fun invoke(i1: Position, i2: Position, i3: Position)
}

object TestEvent1 : FFEvent<TestListenerType>(createTypeKey(TestEvent1::class.java)) {

    private var i1: Int = -1
    private var i2: Int = -1
    private var i3: Int = -1

    override fun notify(listener: TestListenerType) =
        listener(i1, i2, i3)

    fun send(i1: Int, i2: Int, i3: Int) {
        this.i1 = i1
        this.i2 = i2
        this.i3 = i3
        FFContext.notify(this)
    }
}

object TestEvent2 : FFEvent<TestListenerInterface>(createTypeKey(TestEvent2::class.java)) {

    private var i1: Int = -1
    private var i2: Int = -1
    private var i3: Int = -1

    override fun notify(listener: TestListenerInterface) =
        listener(i1, i2, i3)

    fun send(i1: Int, i2: Int, i3: Int) {
        this.i1 = i1
        this.i2 = i2
        this.i3 = i3
        FFContext.notify(this)
    }
}

object TestEvent3 : FFEvent<TestListenerTypeObj>(createTypeKey(TestEvent3::class.java)) {

    private var i1: Position = Position()
    private var i2: Position = Position()
    private var i3: Position = Position()

    override fun notify(listener: TestListenerTypeObj) =
        listener(i1, i2, i3)

    fun send(i1: Position, i2: Position, i3: Position) {
        this.i1 = i1
        this.i2 = i2
        this.i3 = i3
        FFContext.notify(this)
    }
}

object TestEvent4 : FFEvent<TestListenerInterfaceObj>(createTypeKey(TestEvent4::class.java)) {

    private var i1: Position = Position()
    private var i2: Position = Position()
    private var i3: Position = Position()

    override fun notify(listener: TestListenerInterfaceObj) =
        listener(i1, i2, i3)

    fun send(i1: Position, i2: Position, i3: Position) {
        this.i1 = i1
        this.i2 = i2
        this.i3 = i3
        FFContext.notify(this)
    }
}

class EventPerformanceTest {

    @Before
    fun init() {
        TestApp
    }

    @Test
    fun test1() {

        val testListenerType: TestListenerType = {i1, i2, i3 -> i1 + i2 + i3}
        val testListenerInterface: TestListenerInterface = object : TestListenerInterface {
            override fun invoke(i1: Int, i2: Int, i3: Int) {
                i1 + i2 + i3
            }

        }

        FFContext.registerListener(TestEvent1, testListenerType)
        FFContext.registerListener(TestEvent2, testListenerInterface)

        measureTime("primitive type listener", 10000000) {
            TestEvent1.send(1,2,4)
        }

        measureTime("primitive interface listener", 10000000) {
            TestEvent2.send(1,2,4)
        }
    }

    @Test
    fun test2() {

        FFContext.registerListener(TestEvent1, {i1: Int, i2: Int, i3: Int -> i1 + i2 + i3})
        FFContext.registerListener(TestEvent2, object : TestListenerInterface {
            override fun invoke(i1: Int, i2: Int, i3: Int) {
                i1 + i2 + i3
            }
        })

        measureTime("primitive type listener with lambda", 10000000) {
            TestEvent1.send(1,2,4)
        }

        measureTime("primitive interface listener with lambda", 10000000) {
            TestEvent2.send(1,2,4)
        }
    }

    @Test
    fun test3() {
        val pos1 = Position(1, 2)
        val testListenerType: TestListenerTypeObj = {i1, i2, i3 -> i1.x + i2.x + i3.x}
        val testListenerInterface: TestListenerInterfaceObj = object : TestListenerInterfaceObj {
            override fun invoke(i1: Position, i2: Position, i3: Position) {
                i1.x + i2.x + i3.x
            }
        }

        FFContext.registerListener(TestEvent3, testListenerType)
        FFContext.registerListener(TestEvent4, testListenerInterface)

        measureTime("Obj type listener", 10000000) {
            TestEvent3.send(pos1,pos1,pos1)
        }

        measureTime("Obj interface listener", 10000000) {
            TestEvent4.send(pos1,pos1,pos1)
        }
    }

    @Test
    fun test4() {
        val pos1 = Position(1, 2)
        FFContext.registerListener(TestEvent3, {i1: Position, i2: Position, i3: Position -> i1.x + i2.x + i3.x})
        FFContext.registerListener(TestEvent4, object : TestListenerInterfaceObj {
            override fun invoke(i1: Position, i2: Position, i3: Position) {
                i1.x + i2.x + i3.x
            }
        })

        measureTime("Obj type listener with lambda", 10000000) {
            TestEvent3.send(pos1,pos1,pos1)
        }

        measureTime("Obj interface listener with lambda", 10000000) {
            TestEvent4.send(pos1,pos1,pos1)
        }
    }
}