
package engine

class Print constructor(ex: Expression) : Statement {
    val subexpr = ex

    // PRINT հրամանի ինտերպրետացիան
    override fun execute(env: Environment)
    {
        // հաշվել արտահայտության արժեքը
        val vds = subexpr.evaluate(env)
        // արտածել
        println(vds)
    }

    //
    override fun toString() : String =
        "PRINT ${subexpr.toString()}"
}
