
package engine

class Sequence : Statement {
    var items = mutableListOf<Statement>()

    //
    override fun execute(env: Environment)
    {
        for(el in items) {
            el.execute(env)
        }
    }

    //
    override fun toString() : String =
        items.joinToString("\n")
}
