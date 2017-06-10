
package engine

interface Expression {
    fun evaluate(env: Environment) : Value
    fun type(): Type
}
