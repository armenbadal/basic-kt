
package engine

class DoubleValue constructor(val value: Double) : Value, Expression {
    override fun evaluate(env: Environment) : Value = this
}
