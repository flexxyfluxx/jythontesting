package main

import factory.PyObjFactory
import factory.interfaces.HelloWorldSayerType

fun main() {
    println("Hello World!")

    println("And now, we test the magic...")

    val myHWSFactory: PyObjFactory = factory.PyObjFactory(HelloWorldSayerType::class.java, "test", "HelloWorldSayer")

    val myHelloWorldSayer: HelloWorldSayerType = myHWSFactory.make(keywords = Array<String>(0) { it.toString() }) as HelloWorldSayerType

    myHelloWorldSayer.sayHelloWorld()
}