
package engine

class Apply constructor(cl: Subroutine, args: MutableList<Expression>) : Expression {
    val callee = cl
    val arguments = args

    //
    override fun evaluate(env: Environment) : Value
    {
        var locals = Environment()
        if( callee.name.endsWith('$') )
            locals[callee.name] = StringValue("")
        else
            locals[callee.name] = DoubleValue(0.0)

        for((ix, pr) in callee.parameters.withIndex()) {
            locals.put(pr, arguments[ix].evaluate(env))
        }
        callee.body?.execute(locals)
        return locals[callee.name]!!
    }

    //
    override fun toString() : String
    {
        val argvs = arguments.map{ it.toString() }
        return "${callee.name}(${argvs.joinToString(", ")})"
    }
}
