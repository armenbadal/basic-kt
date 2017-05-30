
package engine

class While constructor(cn: Expression, bo: Statement) : Statement {
    val condition = cn
    val body = bo

    //
    override fun execute(env: Environment)
    {
        /*
        var cv = condition.evaluate(env)
        if( cv is DoubleValue ) {
            while( cv.value != 0.0 ) {
                body.execute(env)
                cv = condition.evaluate(env)
            }
        }
        else {
            throw RuntimeError("Condition of WHILE must be numerical.")
        }
        */
    }

    //
    override fun toString() : String =
        "WHILE $condition\n$body\nEND WHILE"
}
