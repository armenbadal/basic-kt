
package engine

sealed class Value : Expression {
    class Number constructor(val value: Double) : Value() {
        override fun evaluate(env: Environment) : Value = this
        override fun toString() : String = value.toString()
    }

    class Text constructor(val value: String) : Value() {
        override fun evaluate(env: Environment) : Value = this
        override fun toString() : String = "\"$value\""
    }
}
