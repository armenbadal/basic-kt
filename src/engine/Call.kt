
package engine

class Call constructor(var callee: Subroutine, val arguments: List<Expression>) : Statement {
    //
    override fun execute(env: Environment)
    {
        Apply(callee, arguments).evaluate(env)
    }
}
