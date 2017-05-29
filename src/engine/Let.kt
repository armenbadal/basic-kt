
package engine

class Let constructor(vr: String, ex: Expression) : Statement {
    val varname = vr
    val exprval = ex

    //
    override fun execute(env: Environment)
    {
        val v0 = exprval.evaluate(env)
        env.put(varname, v0)
    }
}