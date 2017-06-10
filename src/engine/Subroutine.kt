
package engine

//
data class Signature constructor(val result: Type, val inputs: List<Type>) {
    constructor(rs: Type, vararg ins: Type) : this(rs, ins.asList())

    override fun toString(): String =
        inputs.joinToString(separator = " × ") + " → " + result
}

//
abstract class Subroutine constructor(val name: String) {
    abstract operator fun invoke(args: List<Value>): Value
    abstract fun signature(): Signature

    //
    class UserDefined constructor(nm: String, val parameters: List<String>) : Subroutine(nm) {
        var body: Statement? = null

        //
        override operator fun invoke(args: List<Value>): Value
        {
            var locals = Environment()
            parameters.zip(args).forEach({ locals[it.first] = it.second })
            body?.execute(locals)
            return locals.getOrDefault(name, defaultFor(signature().result))
        }

        //
        override fun signature(): Signature =
            Signature(typeOf(name), parameters.map(::typeOf));
    }

    //
    class BuiltIn constructor(nm: String, val result: Type, val inputs: List<Type>) : Subroutine(nm) {
        //
        override operator fun invoke(args: List<Value>): Value
        {
            when( name ) {
                "STR$" -> {
                    val n = args[0] as Value.Number
                    Value.Text(n.toString())
                }
                "MID$" -> {
                    val s = (args[0] as Value.Text).value
                    val b = (args[1] as Value.Number).value
                    val c = (args[2] as Value.Number).value
                    Value.Text(s.substring(b.toInt(), c.toInt()))
                }
                else ->
                    throw RuntimeError("Անհայտ ենթածրագիր. '$name'։")
            }
            return Value.Number(0.0)
        }

        //
        override fun signature(): Signature =
            Signature(result, inputs)
    }
}
