
package engine

data class Subroutine constructor(val name: String, val parameters: MutableList<String>, var body: Statement?) {
    //
    override fun toString() : String
    {
        var res = "SUB $name"
        if( !parameters.isEmpty() ) {
            res += "(${parameters.joinToString(", ")})"
        }
        res += "\n" + body.toString() + "\n"
        res += "END SUB"
        return res
    }
}
