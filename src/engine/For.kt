
package engine

class For constructor(val parameter: String,
                      val begin: Expression,
                      val end: Expression,
                      val step: Expression,
                      val body: Statement) : Statement {
    //
    // FOR հրամանի ինտերպրետացիան
    //
    override fun execute(env: Environment)
    {
        // հաշվարկել պարամետրի սկզբնական արժեքը և համոզվել, որ այն տեքստային չէ
        val bev = begin.evaluate(env) as? Value.Number ?: throw RuntimeError("FOR հրամանի սկզբնական արժեքը պետք է թվային լինի։")

        // հաշվարկել պարամետրի վերջնական (նպատակային) արժեքը և համոզվել, որ այն տեքստային չէ
        val nev = end.evaluate(env) as? Value.Number ?: throw RuntimeError("FOR հրամանի վերջնական արժեքը պետք է թվային լինի։")

        // հաշվել պարամետրի քայլի արժեքը և համոզվել, որ այն տեքստային չէ
        val spv = step.evaluate(env) as? Value.Number ?: throw RuntimeError("FOR հրամանի քայլի արժեքը պետք է թվային լինի։")

        // միջավայրում ավելացնել պարամետրն իր սկզբնական արժեքով
        env.put(parameter, bev)

        loop@
        while( true ) {
            // միջավայրից վերվնել պարամետրի ընթացիկ արժեքը
            val pav = env[parameter] as Value.Number
            // եթե պարամետրի քայլը դրական է, ...
            if( spv.value >= 0 ) {
                // ... ապա ցիկլն ավարտել երբ պարամետրի ընթացիկ արժեքը մեծ է վերջնական արժեքից
                if( pav.value > nev.value ) {
                    break@loop
                }
            }
            else {
                // հակառակ դեպքում ցիկլն ավարտել երբ պարամետրի ընթացիկ արժեքը փոքր է վերջնական արժեքից
                if( pav.value < nev.value )
                    break@loop
            }
            // կատարել ցիկլի մարմինը
            body.execute(env)
            // միջավայրում գրանցել պարամետրի փոփոխված արժեքը
            env[parameter] = Value.Number(pav.value + spv.value)
        }

        // միջավայրից հեռացնել պարամետրը
        env.remove(parameter)
    }
}
