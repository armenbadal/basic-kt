
package engine

class Program constructor(nafa: String) {
    val fileName = nafa
    val subroutines = hashMapOf<String,Subroutine>()

    fun execute()
    {
        for(subr in subroutines)
            println(subr)
    }
}