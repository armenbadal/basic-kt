
package engine

class Call constructor(cl: Subroutine, ags: List<Expression>) : Statement {
    val clapp = Apply(cl, ags)

    //
    override fun execute(env: Environment)
    {
        clapp.evaluate(env)
    }
}
