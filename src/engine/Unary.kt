
package engine

class Unary constructor(oc: Operation, se: Expression) : Expression {
    val opcode = oc
    val subexpr = se

    //
    override fun evaluate(env: Environment) : Value
    {
        val e0 = subexpr.evaluate(env)
        if( e0 is Value.Number) {
            if( opcode == Operation.SUB ) {
                return Value.Number(-e0.value)
            }
            else if( opcode == Operation.NOT ) {
                return Value.Number(if(e0.value == 0.0) 1.0 else 0.0)
            }
        }

        throw RuntimeError("Runtime error")
    }
}