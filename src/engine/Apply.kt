
package engine

class Apply constructor(cl: Subroutine, args: MutableList<Expression>) : Expression {
    val callee = cl
    val arguments = args

    //
    override fun evaluate(env: Environment) : Value
    {
        // նոր միջավայր ենթածրագրի կիրառման համար
        var locals = Environment()
        // ենթածրագրի անունն ավելացնել միջավայրում որպես
        // վերադարձվող արժեքի «տեղ»
        if( callee.name.endsWith('$') )
            locals[callee.name] = Value.Text("")
        else
            locals[callee.name] = Value.Number(0.0)

        // հաշվարկել կիրառման արգւմոնտները ռ համապատասխանեցնել
        // ենթածրագրերի պարամետրերին
        for((ix, pr) in callee.parameters.withIndex()) {
            locals.put(pr, arguments[ix].evaluate(env))
        }
        // կատարել ենթածրագիր մարմինը
        callee.body?.execute(locals)
        // վերցնել վերադարձված արժեքը
        return locals[callee.name]!!
    }

    //
    override fun toString() : String
    {
        val argvs = arguments.map{ it.toString() }
        return "${callee.name}(${argvs.joinToString(", ")})"
    }
}
