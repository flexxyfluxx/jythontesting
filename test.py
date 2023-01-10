from factory.interfaces import HelloWorldSayerType


class HelloWorldSayer(HelloWorldSayerType):
    def sayHelloWorld(self):
        print("Hello from Jython!")
