
package engine

class Let constructor(vr: String, ex: Expression) : Statement {
    val varname = vr
    val exprval = ex

    // LET հրամանի ինտերպրետացիան
    override fun execute(env: Environment)
    {
        // հաշվարկել վերագրվող արժեքը
        val v0 = exprval.evaluate(env)
        // ստուգել վերագրման աջ ու ձախ կողմերի տիպերի համապատասխանությունը
        if( varname.endsWith('$') && v0 is DoubleValue) {
            throw RuntimeError("Տեքստային փոփոխականին վերագրված է թվային արժեք։")
        }
        if( !varname.endsWith('$') && v0 is StringValue) {
            throw RuntimeError("Թվային փոփոխականին վերագրված է տեքստային արժեք։")
        }
        // գրանցել միջավայրում
        env.put(varname, v0)
    }

    override fun toString(): String =
        "LET $varname = ${exprval.toString()}"
}
