
package engine

class If constructor(cn: Expression, dc: Statement, al: Statement?) : Statement {
    val condition = cn
    val decision = dc
    val alternative = al

    //
    override fun execute(env: Environment)
    {
        // TODO
    }
}
