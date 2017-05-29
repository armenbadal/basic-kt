package engine

class For constructor(pr: String, be: Expression, en: Expression, sp: Expression, bo: Statement) : Statement {
    val parameter = pr
    val begin = be
    val end = en
    val step = sp
    val body = bo

    //
    override fun execute(env: Environment)
    {
        // TODO
    }
}
