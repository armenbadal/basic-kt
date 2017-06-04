
package engine

class While constructor(cn: Expression, bo: Statement) : Statement {
    val condition = cn
    val body = bo

    // WHILE հրամանի ինտերպրետացիան
    override fun execute(env: Environment)
    {
        // հաշվել ցիկլի պայմանը և համոզվել, որ արդյուքնը թվային է
        var cv = condition.evaluate(env) as? Value.Number ?: throw RuntimeError("WHILE ցիկլի պայմանը պետք է թվային լինի։")
        // քանի դեռ պայմանի արժեքը տարբեր է զրոյից...
        while( cv.value != 0.0 ) {
            // կատարել ցիկլի մարմինը
            body.execute(env)
            // վերահաշվարկել պայմմանի արժեքը
            cv = condition.evaluate(env) as Value.Number
        }
    }

    //
    override fun toString() : String =
        "WHILE $condition\n$body\nEND WHILE"
}
