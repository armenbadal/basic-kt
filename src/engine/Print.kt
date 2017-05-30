
package engine

class Print constructor(ex: Expression) : Statement {
    val subexpr = ex

    //
    override fun execute(env: Environment)
    {
        val vds = subexpr.evaluate(env)
        println(vds)
    }

    //
    override fun toString() : String =
        "PRINT ${subexpr.toString()}"
}