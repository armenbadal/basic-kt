
package engine

class Print constructor(val subexpr: Expression) : Statement {
    // PRINT հրամանի ինտերպրետացիան
    override fun execute(env: Environment)
    {
        // հաշվել արտահայտության արժեքը
        val vds = subexpr.evaluate(env)
        // արտածել
        println(vds)
    }
}
