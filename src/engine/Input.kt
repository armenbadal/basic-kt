
package engine

class Input constructor(nm: String) : Statement {
    val varname = nm

    //
    override fun execute(env: Environment)
    {
        // TODO read double or string from standard input
    }
}