package factory

import org.python.core.Py
import org.python.core.PyObject
import org.python.core.PyString
import org.python.core.PySystemState

/**
 * Singleton factory object to instantiate Jython-implemented classes in Kotlin.
 */
class PyObjFactory {
    private lateinit var theClass: PyObject
    private lateinit var interfaceType: Class<Any>

    constructor (interfaceType: Class<Any>, systemState: PySystemState, moduleName: String, className: String) {
        initObj(interfaceType, systemState, moduleName, className)
    }

    constructor(interfaceType: Class<Any>, moduleName: String, className: String) {
        initObj(interfaceType, PySystemState(), moduleName, className)
    }

    private fun initObj(interfaceType: Class<Any>, systemState: PySystemState, moduleName: String, className: String) {
        this.interfaceType = interfaceType

        // basically just get the class. from the python system state,idk.
        // read the code and you see how
        val importer = systemState.getBuiltins().__getitem__(Py.newString("__import__"))
        val module = importer.__call__(Py.newString(moduleName))
        theClass = module.__getattr__(className)
    }

    fun make(vararg args: PyString, keywords: Array<String>): Any {
        var convertedArgs: Array<PyObject?> = arrayOfNulls<PyObject>(args.size)
        for (c in args.indices) {
            convertedArgs[c] = Py.java2py(args[c])
        }

        return theClass.__call__(convertedArgs).__tojava__(interfaceType)
    }


}
