
package engine

class If constructor(cn: Expression, dc: Statement, al: Statement?) : Statement {
    val condition = cn
    val decision = dc
    var alternative = al

    //
    override fun execute(env: Environment)
    {
        // TODO
    }

    //
    override fun toString(): String
    {
        var res = "IF $condition THEN\n"
        res += decision.toString()
        var bi = alternative
        while( bi is If ) {
            res += "\nELSEIF ${bi.condition} THEN\n"
            res += bi.decision.toString()
            bi = bi.alternative
        }
        if( bi != null ) {
            res += "\nELSE\n$bi"
        }
        res += "\nEND IF"
        return res
    }
}
