
package engine

class Program constructor(nafa: String) {
    val fileName = nafa
    val subroutines = hashMapOf<String,Subroutine>()

    //
    fun execute()
    {
        for(subr in subroutines)
            println(subr)
    }

    //
    override fun toString() : String
    {
        var res = "'\n' Source: $fileName\n'\n"
        res += subroutines.values.joinToString("\n\n")
        return res
    }
}