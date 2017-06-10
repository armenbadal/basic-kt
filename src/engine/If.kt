
package engine

class If constructor(val condition: Expression, val decision: Statement, var alternative: Statement?) : Statement {
    // IF հրամանի ինտերպրետացիան
    override fun execute(env: Environment)
    {
        // հաշվել ճյուղավորման պայմանը և համոզվել որ այն թվային է
        val cov = condition.evaluate(env) as? Value.Number ?: throw RuntimeError("IF հրամանի պայմանը պետք է թվային լինի։")

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
}
