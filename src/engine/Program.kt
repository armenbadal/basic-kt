
package engine

class Program constructor(nafa: String) {
    val fileName = nafa
    val subroutines = hashMapOf<String,Subroutine>()

    // ամբողջ ծրագիր ինտերպրետացիան
    fun execute()
    {
        // մուտքի կետը Main ենթածրագիրն է
        val entry = subroutines.get("Main")
        // եթե մուտքի կետ գտնվել է, ...
        if( entry != null ) {
            // ապա ստեղծել դատարկ արգումենտներով Call օբյեկտ
            val enex = Call(entry, mutableListOf<Expression>())
            // և կատարել այն դատարկ միջավայրում
            enex.execute(Environment())
        }
    }

    //
    override fun toString() : String
    {
        var res = "'\n' Source: $fileName\n'\n"
        res += subroutines.values.joinToString("\n\n")
        return res
    }
}
