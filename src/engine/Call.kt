package engine

class Call constructor(nm: String, ags: MutableList<Expression>) : Statement {
    val clapp = Apply(nm, ags)

    //
    override fun execute(env: Environment)
    {
        // TODO
    }

    //
    override fun toString() : String =
        "CALL ${clapp.toString()}"
}
