
package engine

class StringValue constructor(val value: String) : Value, Expression {
    override fun evaluate(env: Environment) : Value = this
    override fun toString() : String = "\"$value\""
}
