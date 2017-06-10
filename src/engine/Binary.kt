
package engine

class Binary constructor(val opcode: Operation, val subexpro: Expression, val subexpri: Expression) : Expression {
    //
    override fun evaluate(env: Environment): Value
    {
        val eo = subexpro.evaluate(env)
        val ei = subexpri.evaluate(env)

        if( eo is Value.Number && ei is Value.Number) {
            val res = when( opcode ) {
                Operation.ADD ->
                    eo.value + ei.value
                Operation.SUB ->
                    eo.value - ei.value
                Operation.MUL ->
                    eo.value * ei.value
                Operation.DIV ->
                    eo.value / ei.value
                Operation.MOD ->
                    (eo.value.toInt() % ei.value.toInt()).toDouble()
                Operation.POW ->
                    Math.pow(eo.value,ei.value)
                Operation.EQ ->
                    if( eo.value == ei.value ) 1.0 else 0.0
                Operation.NE ->
                    if( eo.value != ei.value ) 1.0 else 0.0
                Operation.GT ->
                    if( eo.value > ei.value ) 1.0 else 0.0
                Operation.GE ->
                    if( eo.value >= ei.value ) 1.0 else 0.0
                Operation.LT ->
                    if( eo.value < ei.value ) 1.0 else 0.0
                Operation.LE ->
                    if( eo.value <= ei.value ) 1.0 else 0.0
                Operation.AND ->
                    if( (eo.value != 0.0) && (ei.value != 0.0) ) 1.0 else 0.0
                Operation.OR ->
                    if( (eo.value != 0.0) || (ei.value != 0.0) ) 1.0 else 0.0
                else ->
                    throw RuntimeError("Unknown binary operation with numbers: '$opcode'.")
            }
            return Value.Number(res)
        }

        if( eo is Value.Text && ei is Value.Text) {
            // տողերի կոնկատենացիա
            if( opcode == Operation.CONC ) {
                return Value.Text(eo.value + ei.value)
            }
            // տողերի համեմատում
            val res = when( opcode ) {
                Operation.EQ ->
                    if( eo.value == ei.value ) 1.0 else 0.0
                Operation.NE ->
                    if( eo.value != ei.value ) 1.0 else 0.0
                Operation.GT ->
                    if( eo.value > ei.value ) 1.0 else 0.0
                Operation.GE ->
                    if( eo.value >= ei.value ) 1.0 else 0.0
                Operation.LT ->
                    if( eo.value < ei.value ) 1.0 else 0.0
                Operation.LE ->
                    if( eo.value <= ei.value ) 1.0 else 0.0
                else ->
                    throw RuntimeError("Unknown binary operation with strings: '$opcode'.")
            }
            return Value.Number(res)
        }

        throw RuntimeError("Uncompatible operators for '$opcode'.")
    }

    //
    override fun type(): Type
    {
        val vo = subexpro.type()
        val vi = subexpri.type()

        if( vo == Type.NUMBER && vi == Type.NUMBER ) {
            if( opcode != Operation.CONC ) {
                return Type.NUMBER
            }
        }
        else if( vo == Type.TEXT && vi == Type.TEXT ) {
            if( opcode == Operation.CONC ) {
                return Type.TEXT
            }
            if( opcode in Operation.EQ..Operation.LE ) {
                return Type.NUMBER
            }
        }

        throw TypeError("Տիպերի անհամապատասխանություն։")
    }
}
