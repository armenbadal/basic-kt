package engine

class Binary constructor(oc: Operation, seo: Expression, sei: Expression) : Expression {
    val opcode = oc
    val subexpro = seo
    val subexpri = sei

    //
    override fun evaluate(env: Environment) : Value
    {
        val eo = subexpro.evaluate(env)
        val ei = subexpri.evaluate(env)
        if( eo is DoubleValue && ei is DoubleValue ) {
            return when( opcode ) {
                Operation.ADD ->
                    DoubleValue(eo.value + ei.value)
                Operation.SUB ->
                    DoubleValue(eo.value - ei.value)
                Operation.MUL ->
                    DoubleValue(eo.value * ei.value)
                Operation.DIV ->
                    DoubleValue(eo.value / ei.value)
                Operation.MOD ->
                    DoubleValue((eo.value.toInt() % ei.value.toInt()).toDouble())
                Operation.POW ->
                    DoubleValue(Math.pow(eo.value,ei.value))
                Operation.EQ ->
                    DoubleValue(if( eo.value == ei.value ) 1.0 else 0.0)
                Operation.NE ->
                    DoubleValue(if( eo.value != ei.value ) 1.0 else 0.0)
                Operation.GT ->
                    DoubleValue(if( eo.value > ei.value ) 1.0 else 0.0)
                Operation.GE ->
                    DoubleValue(if( eo.value >= ei.value ) 1.0 else 0.0)
                Operation.LT ->
                    DoubleValue(if( eo.value < ei.value ) 1.0 else 0.0)
                Operation.LE ->
                    DoubleValue(if( eo.value <= ei.value ) 1.0 else 0.0)
                Operation.AND ->
                    DoubleValue(if( (eo.value != 0.0) && (ei.value != 0.0) ) 1.0 else 0.0)
                Operation.OR ->
                    DoubleValue(if( (eo.value != 0.0) || (ei.value != 0.0) ) 1.0 else 0.0)
                else ->
                    throw RuntimeError("Unknown binary operation with numbers: '$opcode'.")
            }
        }
        else if( eo is StringValue && ei is StringValue ) {
            if( opcode == Operation.CONC ) {
                return StringValue(eo.value + ei.value)
            }
            else {
                throw RuntimeError("Unknown binary operation with strings: '$opcode'.")
            }
        }
        else {
            throw RuntimeError("Uncompatible operators for '$opcode'.")
        }
    }

    //
    override fun toString() : String =
        "($subexpro $opcode $subexpri)"
}
