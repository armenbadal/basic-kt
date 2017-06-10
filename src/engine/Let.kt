
package engine

class Let constructor(val varname: String, val exprval: Expression) : Statement {
    // LET հրամանի ինտերպրետացիան
    override fun execute(env: Environment)
    {
        // հաշվարկել վերագրվող արժեքը
        val v0 = exprval.evaluate(env)
        // գրանցել միջավայրում
        env.put(varname, v0)
    }
}
