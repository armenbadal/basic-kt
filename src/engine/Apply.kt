
package engine

class Apply constructor(var callee: Subroutine, val arguments: List<Expression>) : Expression {
    //
    override fun evaluate(env: Environment): Value
    {
        return callee(arguments.map{ it.evaluate(env) })
    }

    //
    override fun type(): Type = callee.signature().result
}
