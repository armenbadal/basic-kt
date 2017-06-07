
package engine

class Let constructor(val varname: String, val exprval: Expression) : Statement {
    // LET հրամանի ինտերպրետացիան
    override fun execute(env: Environment)
    {
        // հաշվարկել վերագրվող արժեքը
        val v0 = exprval.evaluate(env)
        // ստուգել վերագրման աջ ու ձախ կողմերի տիպերի համապատասխանությունը
        if( varname.endsWith('$') && v0 is Value.Number) {
            throw RuntimeError("Տեքստային փոփոխականին վերագրված է թվային արժեք։")
        }
        if( !varname.endsWith('$') && v0 is Value.Text) {
            throw RuntimeError("Թվային փոփոխականին վերագրված է տեքստային արժեք։")
        }
        // գրանցել միջավայրում
        env.put(varname, v0)
    }

    override fun toString(): String =
        "LET $varname = ${exprval.toString()}"
}
