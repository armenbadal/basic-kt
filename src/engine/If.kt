
package engine

class If constructor(cn: Expression, dc: Statement, al: Statement?) : Statement {
    val condition = cn
    val decision = dc
    var alternative = al

    // IF հրամանի ինտերպրետացիան
    override fun execute(env: Environment)
    {
        // հաշվել ճյուղավորման պայմանը և համոզվել որ այն թվային է
        val cov = condition.evaluate(env) as? DoubleValue ?: throw RuntimeError("IF հրամանի պայմանը պետք է թվային լինի։")

        // եթե պայմանի արժեքը տարբեր է զրոյից, ...
        if( cov.value != 0.0 ) {
            // ... ապա կատարել դրական ճյուղը
            decision.execute(env)
        }
        else {
            // ... հակառակ դեպքում կատարել այլընտրանքային ճյուղը,
            // եթե, իհարկե, այն առկա է
            if( alternative != null ) alternative!!.execute(env)
        }
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
