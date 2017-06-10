
package engine

//
enum class Type {
    NUMBER,
    TEXT
}

//
fun typeOf(vr: String): Type =
        if( vr.endsWith('$') ) Type.TEXT else Type.NUMBER

//
fun defaultFor(x: Type) : Value =
        if( x == Type.TEXT ) Value.Text("") else Value.Number(0.0)

//
sealed class Value : Expression {
    class Number constructor(val value: Double) : Value() {
        override fun evaluate(env: Environment) : Value = this
        override fun type(): Type = Type.NUMBER
        override fun toString() : String = value.toString()
    }

    class Text constructor(val value: String) : Value() {
        override fun evaluate(env: Environment) : Value = this
        override fun type(): Type = Type.TEXT
        override fun toString() : String = value
    }
}
