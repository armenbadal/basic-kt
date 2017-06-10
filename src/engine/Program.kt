
package engine

class Program constructor(val filename: String) {
    val subroutines = hashMapOf<String,Subroutine>()

    init {
        subroutines["STR$"] = Subroutine.BuiltIn("STR$", Type.TEXT, listOf(Type.NUMBER))
    }

    // ամբողջ ծրագիր ինտերպրետացիան
    fun execute()
    {
        // մուտքի կետը Main ենթածրագիրն է
        val entry = subroutines.get("Main")
        // եթե մուտքի կետ գտնվել է, ...
        if( entry != null ) {
            // ապա ստեղծել դատարկ արգումենտներով Call օբյեկտ
            val enex = Call(entry, listOf<Expression>())
            // և կատարել այն դատարկ միջավայրում
            enex.execute(Environment())
        }
    }
}
