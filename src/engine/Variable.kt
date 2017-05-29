
package engine

class Variable constructor(nm: String) : Expression {
    val name = nm

    //
    override fun evaluate(env: Environment) : Value
    {
        if( env.containsKey(name) ) {
            return env[name]!!
        }

        throw RuntimeError("Uninitialized variable '$name'.")
    }
}