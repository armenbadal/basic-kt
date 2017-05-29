
package engine

class Unary constructor(oc: Operation, se: Expression) : Expression {
    val opcode = oc
    val subexpr = se

    //
    override fun evaluate(env: Environment) : Value
    {
        val e0 = subexpr.evaluate(env)
        if( e0 is DoubleValue ) {
            if( opcode == Operation.SUB ) {
                return DoubleValue(-e0.value)
            }
            else if( opcode == Operation.NOT ) {
                return DoubleValue(if(e0.value == 0.0) 1.0 else 0.0)
            }
        }

        throw RuntimeError("Runtime error")
    }
}