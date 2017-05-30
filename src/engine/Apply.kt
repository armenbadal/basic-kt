
package engine

class Apply constructor(nm: String, args: MutableList<Expression>) : Expression {
    val name = nm
    val arguments = args

    //
    override fun evaluate(env: Environment) : Value
    {
        ///val acrargs = arguments.map{ it.evaluate(env) }
        return DoubleValue(0.0)
    }

    //
    override fun toString() : String
    {
        val argvs = arguments.map{ it.toString() }
        return "$name(${argvs.joinToString(", ")})"
    }
}