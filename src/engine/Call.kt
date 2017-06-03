package engine

class Call constructor(cl: Subroutine, ags: MutableList<Expression>) : Statement {
    val clapp = Apply(cl, ags)

    //
    override fun execute(env: Environment)
    {
        clapp.evaluate(env)
    }

    //
    override fun toString() : String =
        "CALL ${clapp.toString()}"
}
