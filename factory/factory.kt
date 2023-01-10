package factory

import org.python.core.Py
import org.python.core.PyObject
import org.python.core.PySystemState

/**
 * Factory class to instantiate Jython objects in Kotlin.
 */
class PyObjFactory {
    private lateinit var theClass: PyObject
    private lateinit var interfaceType: Class<*>
    companion object {}

    // init w/ existing python sysstate
    constructor (interfaceType: Class<*>, systemState: PySystemState, moduleName: String, className: String) {
        loadClass(interfaceType, systemState, moduleName, className)
    }

    // init w/out sysstate
    // FIXME possible runtime overhead due to instantiation of new python sysstate?
    constructor(interfaceType: Class<*>, moduleName: String, className: String) {
        loadClass(interfaceType, PySystemState(), moduleName, className)
    }

    // load class from Python
    fun loadClass(interfaceType: Class<*>, systemState: PySystemState, moduleName: String, className: String) {
        this.interfaceType = interfaceType

        // basically just get the class. from the python system state,idk.
        // read the code and you see how
        val importer = systemState.getBuiltins().__getitem__(Py.newString("__import__"))
        val module = importer.__call__(Py.newString(moduleName))
        theClass = module.__getattr__(className)
    }

    // instantiate and return loaded class
    fun make(vararg args: Array<Any>, keywords: Array<String>): Any {
        val convertedArgs: Array<PyObject> = (args.map { Py.java2py(it) }).toTypedArray()

        return theClass.__call__(convertedArgs, keywords).__tojava__(interfaceType)
    }
}
